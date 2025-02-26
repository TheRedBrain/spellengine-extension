package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.entity.damage.DuckDamageSourcesMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellCostMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionDamageMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellImpactActionHealMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellLaunchPropertiesMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellProjectileDataPerksMixin;
import com.google.common.base.Suppliers;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.effect.EntityImmunity;
import net.spell_engine.api.effect.StatusEffectClassification;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellEvents;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.tags.SpellEngineEntityTags;
import net.spell_engine.entity.ConfigurableKnockback;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.internals.Ammo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellTriggers;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCastSyncHelper;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.internals.container.SpellContainerSource;
import net.spell_engine.internals.target.EntityRelations;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_engine.utils.AnimationHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_engine.utils.VectorHelper;
import net.spell_engine.utils.WorldScheduler;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchool;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(SpellHelper.class)
@SuppressWarnings("UnreachableCode")
public abstract class SpellHelperMixin {

	@Shadow(remap = false)
	private static boolean launchSequenceEligible(int index, int rule) {
		throw new AssertionError();
	}

	/**
	 * @author TheRedBrain
	 * @reason check for custom cost
	 */
	@Overwrite
	public static SpellCast.Attempt attemptCasting(PlayerEntity player, ItemStack itemStack, Identifier spellId, boolean checkAmmo) {
		SpellCasterEntity caster = (SpellCasterEntity) player;
		RegistryEntry.Reference<Spell> spellEntry = (RegistryEntry.Reference) SpellRegistry.from(player.getWorld()).getEntry(spellId).orElse(null);
		if (spellEntry == null) {
			return SpellCast.Attempt.none();
		} else {
			Spell spell = (Spell) spellEntry.value();
			if (caster.getCooldownManager().isCoolingDown(spellId)) {
				return SpellCast.Attempt.failOnCooldown(new SpellCast.Attempt.OnCooldownInfo());
			}

			if (checkAmmo) {
				Ammo.Result ammoResult = Ammo.ammoForSpell(player, spell, itemStack);
				if (!ammoResult.satisfied()) {
					return SpellCast.Attempt.failMissingItem(new SpellCast.Attempt.MissingItemInfo(ammoResult.item()));
				}
			}
			ServerConfig spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;

			if (spellEngineExtensionConfig.spell_cost_health_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkHealthCost()) {
				float healthCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getHealthCost();
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
					healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
				}
				if (healthCost > 0 && healthCost > player.getHealth()) {
					player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_health"), true);
					return SpellCast.Attempt.none();
				}
			}
			if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkManaCost()) {
				float manaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getManaCost();
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
					manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
				}
				float currentMana = SpellEngineExtension.getCurrentMana(player);
				if (manaCost > 0 && manaCost > currentMana) {
					player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_mana"), true);
					return SpellCast.Attempt.none();
				}
			}
			if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get() && ((DuckSpellCostMixin) spell.cost).spellengineextension$checkStaminaCost()) {
				float staminaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getStaminaCost();
				if (((DuckSpellCostMixin) spell.cost).spellengineextension$staminaCostMultiplierApplies()) {
					staminaCost = staminaCost * ((DuckLivingEntityMixin) player).spellengineextension$getStaminaSpellCostMultiplier();
				}
				float currentStamina = SpellEngineExtension.getCurrentStamina(player);
				if (staminaCost > 0 && staminaCost > currentStamina) {
					player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_stamina"), true);
					return SpellCast.Attempt.none();
				}
			}
			if (spellEngineExtensionConfig.spell_cost_effects_allowed.get() && spell.cost.effect_id != null) {
				Optional<RegistryEntry.Reference<StatusEffect>> effect = Registries.STATUS_EFFECT.getEntry(Identifier.tryParse(spell.cost.effect_id));
				if (effect.isPresent()) {
					if (!player.hasStatusEffect(effect.get())) {
						player.sendMessage(Text.translatable("hud.cast_attempt_error.missing_status_effect", Text.translatable(effect.get().value().getTranslationKey()).getString()), true);
						return SpellCast.Attempt.none();
					} else {
						StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect.get());
						if (statusEffectInstance != null) {
							int decrementEffectAmount = ((DuckSpellCostMixin) spell.cost).spellengineextension$getDecrementEffectAmount();
							if (decrementEffectAmount > 0 && statusEffectInstance.getAmplifier() + 1 < decrementEffectAmount) {
								player.sendMessage(Text.translatable("hud.cast_attempt_error.status_effect_amplifier_too_low", Text.translatable(effect.get().value().getTranslationKey()).getString()), true);
								return SpellCast.Attempt.none();
							}
						}
					}
				}
			}
			return SpellCast.Attempt.success();
		}
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate health cost, mana cost, stamina cost, reducing amplifier of status effect cost instead of removing them, self consuming of casting item
	 */
	@Overwrite
	public static void performSpell(World world, PlayerEntity player, RegistryEntry<Spell> spellEntry, SpellTarget.SearchResult targetResult, SpellCast.Action action, float progress) {
		if (!player.isSpectator()) {
			Spell spell = (Spell) spellEntry.value();
			Identifier spellId = ((RegistryKey) spellEntry.getKey().get()).getValue();
			ItemStack heldItemStack = player.getMainHandStack();
			SpellContainerSource.SourcedContainer spellSource = SpellContainerSource.getFirstSourceOfSpell(spellId, player);
			if (spellSource != null) {
				SpellCast.Attempt attempt = SpellHelper.attemptCasting(player, heldItemStack, spellId);
				if (attempt.isSuccess()) {
					SpellCasterEntity caster = (SpellCasterEntity) player;
					List<Entity> targets = targetResult.entities();
					float castingSpeed = caster.getCurrentCastingSpeed();
					progress = Math.max(Math.min(progress, 1.0F), 0.0F);
					float channelMultiplier = 1.0F;
					int channelTickIndex = 0;
					int incrementChannelTicks = 0;
					boolean shouldPerformImpact = true;
					Supplier<Collection<ServerPlayerEntity>> trackingPlayers = Suppliers.memoize(() -> {
						return PlayerLookup.tracking(player);
					});
					switch (action) {
						case CHANNEL:
							channelTickIndex = caster.getChannelTickIndex();
							incrementChannelTicks = 1;
							channelMultiplier = SpellHelper.channelValueMultiplier(spell);
							break;
						case RELEASE:
							if (SpellHelper.isChanneled(spell)) {
								shouldPerformImpact = false;
								channelMultiplier = 1.0F;
							} else {
								channelMultiplier = progress >= 1.0F ? 1.0F : 0.0F;
							}

							SpellCastSyncHelper.clearCasting(player);
						case TRIGGER:
					}

					Ammo.Result ammoResult = Ammo.ammoForSpell(player, spell, heldItemStack);
					if (channelMultiplier > 0.0F && ammoResult.satisfied()) {
						Spell.Target targeting = spell.target;
						boolean released = action == SpellCast.Action.RELEASE || action == SpellCast.Action.TRIGGER && spell.type == Spell.Type.PASSIVE;
						boolean success = true;
						if (shouldPerformImpact) {
							success = false;
							SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(channelMultiplier, 1.0F, (Vec3d) null, SpellPower.getSpellPower(spell.school, player), SpellHelper.focusMode(spell), channelTickIndex);
							if (targeting.cap > 0) {
								targets = targets.stream().sorted(Comparator.comparingDouble((targetx) -> {
									return targetx.squaredDistanceTo(player.getPos());
								})).limit((long) targeting.cap).toList();
							}
							List<SpellHelper.TargetWithContext> targetsWithContext;
							switch (targeting.type) {
								case NONE:
									success = SpellHelper.deliver(world, spellEntry, player, List.of(), context, (Vec3d) null);
									break;
								case CASTER:
									targetsWithContext = List.of(new SpellHelper.TargetWithContext(player, context));
									success = SpellHelper.deliver(world, spellEntry, player, targetsWithContext, context, (Vec3d) null);
									break;
								case AIM:
									Spell.Target.Aim aim = targeting.aim;
									Optional<Entity> firstTarget = targets.stream().findFirst();
									targetsWithContext = List.of();
									if (firstTarget.isPresent()) {
										Entity target = (Entity) firstTarget.get();
										SpellHelper.ImpactContext targetSpecificContext = context;
										targetsWithContext = List.of(new SpellHelper.TargetWithContext(target, targetSpecificContext));
									}

									if (!aim.required || firstTarget.isPresent()) {
										success = SpellHelper.deliver(world, spellEntry, player, targetsWithContext, context, targetResult.location());
									}
									break;
								case AREA:
									Vec3d center = player.getPos().add(0.0, (double) (player.getHeight() / 2.0F), 0.0);
									Spell.Target.Area area = spell.target.area;
									float range = SpellHelper.getRange(player, spell) * player.getScale();
									SpellHelper.ImpactContext centeredContext = context;
									double squaredRange = (double) (range * range);
									targetsWithContext = targets.stream().map((targetx) -> {
										float distanceBasedMultiplier = 1.0F;
										switch (area.distance_dropoff) {
											case SQUARED:
												distanceBasedMultiplier = (float) ((squaredRange - targetx.squaredDistanceTo(center)) / squaredRange);
												distanceBasedMultiplier = Math.max(distanceBasedMultiplier, 0.0F);
											case NONE:
											default:
												return new SpellHelper.TargetWithContext(targetx, centeredContext.distance(distanceBasedMultiplier));
										}
									}).toList();
									SpellHelper.deliver(world, spellEntry, player, targetsWithContext, context, (Vec3d) null);
									success = true;
									break;
								case BEAM:
								case FROM_TRIGGER:
									targetsWithContext = targets.stream().map((targetx) -> {
										return new SpellHelper.TargetWithContext(targetx, context);
									}).toList();
									success = SpellHelper.deliver(world, spellEntry, player, targetsWithContext, context, (Vec3d) null);
									break;
								default:
									throw new IllegalStateException("Unexpected value: " + String.valueOf(targeting.type));
							}

							caster.setChannelTickIndex(channelTickIndex + incrementChannelTicks);
						}

						if (released && success) {
							ParticleHelper.sendBatches(player, spell.release.particles);
							SoundHelper.playSound(world, player, spell.release.sound);
							AnimationHelper.sendAnimation(player, (Collection) trackingPlayers.get(), SpellCast.Animation.RELEASE, spell.release.animation, castingSpeed);
							SpellHelper.imposeCooldown(player, spellSource, spellId, spell, progress);
							player.addExhaustion(spell.cost.exhaust * SpellEngineMod.config.spell_cost_exhaust_multiplier);

							var spellEngineExtensionConfig = SpellEngineExtension.SERVER_CONFIG;

							// health cost
							if (spellEngineExtensionConfig.spell_cost_health_allowed.get()) {
								float healthCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getHealthCost();
								if (((DuckSpellCostMixin) spell.cost).spellengineextension$healthCostMultiplierApplies()) {
									healthCost = healthCost * ((DuckLivingEntityMixin) player).spellengineextension$getHealthSpellCostMultiplier();
								}
								if (healthCost > 0.0F) {
									player.damage(((DuckDamageSourcesMixin) player.getDamageSources()).betteradventuremode$bloodMagicCasting(), healthCost);
								}
							}

							// mana cost
							if (SpellEngineExtension.isManaAttributesLoaded && spellEngineExtensionConfig.spell_cost_mana_allowed.get()) {
								float manaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getManaCost();
								if (((DuckSpellCostMixin) spell.cost).spellengineextension$manaCostMultiplierApplies()) {
									manaCost = manaCost * ((DuckLivingEntityMixin) player).spellengineextension$getManaSpellCostMultiplier();
								}
								if (manaCost > 0.0F) {
									SpellEngineExtension.addMana(player, -manaCost);
								}
							}

							// stamina cost
							if (SpellEngineExtension.isStaminaAttributesLoaded && spellEngineExtensionConfig.spell_cost_stamina_allowed.get()) {
								float staminaCost = ((DuckSpellCostMixin) spell.cost).spellengineextension$getStaminaCost();
								if (((DuckSpellCostMixin) spell.cost).spellengineextension$staminaCostMultiplierApplies()) {
									staminaCost = staminaCost * ((DuckLivingEntityMixin) player).spellengineextension$getStaminaSpellCostMultiplier();
								}
								if (staminaCost > 0.0F) {
									SpellEngineExtension.addStamina(player, -staminaCost);
								}
							}

//								// consume spell casting item
								if (((DuckSpellCostMixin) spell.cost).spellengineextension$consumeSelf()) {
									player.incrementStat(Stats.USED.getOrCreateStat(heldItemStack.getItem()));
									if (!player.isCreative()) {
										heldItemStack.decrement(1);
									}
								}

							if (SpellEngineMod.config.spell_cost_durability_allowed && spell.cost.durability > 0) {
								ItemStack stackToDamage = spellSource.itemStack().isDamageable() ? spellSource.itemStack() : heldItemStack;
								stackToDamage.damage(spell.cost.durability, player, EquipmentSlot.MAINHAND);
							}

							Ammo.consume(ammoResult, player);
							if (spell.cost.effect_id != null) {
								Optional<RegistryEntry.Reference<StatusEffect>> effect = Registries.STATUS_EFFECT.getEntry(Identifier.of(spell.cost.effect_id));
								if (effect.isPresent()) {
									int decrementEffectAmount = ((DuckSpellCostMixin) spell.cost).spellengineextension$getDecrementEffectAmount();
									if (decrementEffectAmount < 0) {
										player.removeStatusEffect(effect.get());
									} else if (decrementEffectAmount > 0) {
										int newAmplifier = -1;
										StatusEffectInstance statusEffectInstance = player.getStatusEffect(effect.get());
										if (statusEffectInstance != null) {
											int oldAmplifier = statusEffectInstance.getAmplifier();
											newAmplifier = oldAmplifier - decrementEffectAmount;
										}
										player.removeStatusEffect(effect.get());
										if (newAmplifier >= 0) {
											player.addStatusEffect(new StatusEffectInstance(effect.get(), statusEffectInstance.getDuration(), newAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon()));
										}
									}
								}
							}

							SpellEvents.SpellCastEvent.Args args = new SpellEvents.SpellCastEvent.Args(player, spellEntry, targets, action, progress);
							SpellEvents.SPELL_CAST.invoke((listener) -> {
								listener.onSpellCast(args);
							});
						}
					}
				}
			}
		}
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate perks and launch properties entity attributes
	 */
	@Overwrite
	public static void shootProjectile(World world, LivingEntity caster, Entity target, RegistryEntry<Spell> spellEntry, SpellHelper.ImpactContext context, int sequenceIndex) {
		if (!world.isClient) {
			Spell spell = (Spell) spellEntry.value();
			Vec3d launchPoint = SpellHelper.launchPoint(caster);
			Spell.Delivery.ShootProjectile data = spell.deliver.projectile;
			Spell.ProjectileData projectileData = data.projectile;
			Spell.ProjectileData.Perks mutablePerks = projectileData.perks.copy();

			// region modifying mutable perks
			ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;
			if (serverConfig.spell_projectile_perk_extra_ricochet_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraRicochetAttribute()) {
				mutablePerks.ricochet += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochet());
			}

			if (serverConfig.spell_projectile_perk_extra_ricochet_range_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraRicochetRangeAttribute()) {
				mutablePerks.ricochet_range += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochetRange());
			}

			if (serverConfig.spell_projectile_perk_extra_bounce_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraBounceAttribute()) {
				mutablePerks.bounce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraBounce());
			}

			if (serverConfig.spell_projectile_perk_extra_pierce_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraPierceAttribute()) {
				mutablePerks.pierce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraPierce());
			}

			if (serverConfig.spell_projectile_perk_extra_chain_reaction_size_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraChainReactionSizeAttribute()) {
				mutablePerks.chain_reaction_size += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionSize());
			}

			if (serverConfig.spell_projectile_perk_extra_chain_reaction_triggers_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraChainReactionTriggersAttribute()) {
				mutablePerks.chain_reaction_triggers += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionTriggers());
			}
			// endregion modifying mutable perks

			SpellProjectile projectile = new SpellProjectile(world, caster, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FLY, spellEntry, context, mutablePerks);
			Spell.LaunchProperties mutableLaunchProperties = data.launch_properties.copy();

			// region modifying mutable launch properties
			if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) mutableLaunchProperties).spellengineextension$respectExtraLaunchCountAttribute()) {
				mutableLaunchProperties.extra_launch_count += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchCount());
			}

			if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) mutableLaunchProperties).spellengineextension$respectExtraLaunchDelayAttribute()) {
				mutableLaunchProperties.extra_launch_delay += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchDelay());
			}

			if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) mutableLaunchProperties).spellengineextension$respectExtraVelocityAttribute()) {
				mutableLaunchProperties.velocity += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraVelocity());
			}
			// endregion modifying mutable launch properties

			if (SpellEvents.PROJECTILE_SHOOT.isListened()) {
				SpellEvents.PROJECTILE_SHOOT.invoke((listener) -> {
					listener.onProjectileLaunch(new SpellEvents.ProjectileLaunchEvent(projectile, mutableLaunchProperties, caster, target, spellEntry, context, sequenceIndex));
				});
			}

			float velocity = mutableLaunchProperties.velocity;
			float divergence = projectileData.divergence;
			float directionPitch = caster.getPitch();
			float directionYaw = caster.getYaw();
			Vec3d look;
			if (data.direct_towards_target && target != null) {
				look = target.getPos().subtract(caster.getPos()).normalize();
				directionPitch = (float) VectorHelper.pitchFromNormalized(look);
				directionYaw = (float) VectorHelper.yawFromNormalized(look);
			}

			int ticks;
			int i;
			if (data.inherit_shooter_velocity) {
				projectile.setVelocity(caster, directionPitch, directionYaw, 0.0F, velocity, divergence);
			} else {
				if (data.direction_offsets != null && data.direction_offsets.length > 0 && (!data.direction_offsets_require_target || target != null)) {
					i = context.isChanneled() ? context.channelTickIndex() : sequenceIndex;
					ticks = i % data.direction_offsets.length;
					Spell.Delivery.ShootProjectile.DirectionOffset offset = data.direction_offsets[ticks];
					directionPitch += offset.pitch;
					directionYaw += offset.yaw;
				}

				look = caster.getRotationVector(directionPitch, directionYaw).normalize();
				projectile.setVelocity(look.x, look.y, look.z, velocity, divergence);
			}

			projectile.range = spell.range;
			projectile.setPitch(directionPitch);
			projectile.setYaw(directionYaw);
			projectile.setFollowedTarget(target);
			world.spawnEntity(projectile);
			SoundHelper.playSound(world, projectile, mutableLaunchProperties.sound);
			if (sequenceIndex == 0 && mutableLaunchProperties.extra_launch_count > 0) {
				for (i = 0; i < mutableLaunchProperties.extra_launch_count; ++i) {
					ticks = (i + 1) * mutableLaunchProperties.extra_launch_delay;
					int nextSequenceIndex = i + 1;
					((WorldScheduler) world).schedule(ticks, () -> {
						if (caster != null && caster.isAlive()) {
							shootProjectile(world, caster, target, spellEntry, context, nextSequenceIndex);
						}
					});
				}
			}

		}
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate perks and launch properties entity attributes
	 */
	@Overwrite
	public static boolean fallProjectile(World world, LivingEntity caster, Entity target, @Nullable Vec3d targetLocation, RegistryEntry<Spell> spellEntry, SpellHelper.ImpactContext context, int sequenceIndex) {
		if (world.isClient) {
			return false;
		} else {
			Vec3d targetPosition = target != null ? target.getPos() : targetLocation;
			if (targetPosition == null) {
				return false;
			} else {
				Spell spell = (Spell) spellEntry.value();
				Spell.Delivery.Meteor meteor = spell.deliver.meteor;
				float height = meteor.launch_height;
				Vec3d launchPoint = targetPosition.add(0.0, (double) height, 0.0);
				Spell.Delivery.Meteor data = spell.deliver.meteor;
				Spell.ProjectileData projectileData = data.projectile;
				Spell.LaunchProperties mutableLaunchProperties = data.launch_properties.copy();

				// region modifying mutable launch properties
				ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;
				if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) mutableLaunchProperties).spellengineextension$respectExtraLaunchCountAttribute()) {
					mutableLaunchProperties.extra_launch_count += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchCount());
				}

				if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) mutableLaunchProperties).spellengineextension$respectExtraLaunchDelayAttribute()) {
					mutableLaunchProperties.extra_launch_delay += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraLaunchDelay());
				}

				if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) mutableLaunchProperties).spellengineextension$respectExtraVelocityAttribute()) {
					mutableLaunchProperties.velocity += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraVelocity());
				}
				// endregion modifying mutable launch properties

				Spell.ProjectileData.Perks mutablePerks = projectileData.perks.copy();

				// region modifying mutable perks
				if (serverConfig.spell_projectile_perk_extra_ricochet_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraRicochetAttribute()) {
					mutablePerks.ricochet += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochet());
				}

				if (serverConfig.spell_projectile_perk_extra_ricochet_range_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraRicochetRangeAttribute()) {
					mutablePerks.ricochet_range += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraRicochetRange());
				}

				if (serverConfig.spell_projectile_perk_extra_bounce_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraBounceAttribute()) {
					mutablePerks.bounce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraBounce());
				}

				if (serverConfig.spell_projectile_perk_extra_pierce_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraPierceAttribute()) {
					mutablePerks.pierce += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraPierce());
				}

				if (serverConfig.spell_projectile_perk_extra_chain_reaction_size_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraChainReactionSizeAttribute()) {
					mutablePerks.chain_reaction_size += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionSize());
				}

				if (serverConfig.spell_projectile_perk_extra_chain_reaction_triggers_attribute_allowed.get() && ((DuckSpellProjectileDataPerksMixin) mutablePerks).spellengineextension$respectExtraChainReactionTriggersAttribute()) {
					mutablePerks.chain_reaction_triggers += (int) (((DuckLivingEntityMixin) caster).spellengineextension$getExtraChainReactionTriggers());
				}
				// endregion modifying mutable perks

				SpellProjectile projectile = new SpellProjectile(world, caster, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FALL, spellEntry, context, mutablePerks);
				if (SpellEvents.PROJECTILE_FALL.isListened()) {
					SpellEvents.PROJECTILE_FALL.invoke((listener) -> {
						listener.onProjectileLaunch(new SpellEvents.ProjectileLaunchEvent(projectile, mutableLaunchProperties, caster, target, spellEntry, context, sequenceIndex));
					});
				}

				projectile.setYaw(0.0F);
				projectile.setPitch(90.0F);
				if (launchSequenceEligible(sequenceIndex, meteor.divergence_requires_sequence)) {
					projectile.setVelocity(0.0, -1.0, 0.0, mutableLaunchProperties.velocity, 0.5F, projectileData.divergence);
				} else {
					projectile.setVelocity(new Vec3d(0.0, (double) (-mutableLaunchProperties.velocity), 0.0));
				}

				if (launchSequenceEligible(sequenceIndex, meteor.follow_target_requires_sequence)) {
					projectile.setFollowedTarget(target);
				} else {
					projectile.setFollowedTarget((Entity) null);
				}

				if (meteor.launch_radius > 0.0F && launchSequenceEligible(sequenceIndex, meteor.offset_requires_sequence)) {
					double randomAngle = Math.toRadians((double) (world.random.nextFloat() * 360.0F));
					Vec3d offset = (new Vec3d((double) meteor.launch_radius, 0.0, 0.0)).rotateY((float) randomAngle);
					projectile.setPosition(projectile.getPos().add(offset));
				}

				projectile.prevYaw = projectile.getYaw();
				projectile.prevPitch = projectile.getPitch();
				projectile.range = height;
				world.spawnEntity(projectile);
				if (sequenceIndex == 0 && mutableLaunchProperties.extra_launch_count > 0) {
					for (int i = 0; i < mutableLaunchProperties.extra_launch_count; ++i) {
						int ticks = (i + 1) * mutableLaunchProperties.extra_launch_delay;
						int nextSequenceIndex = i + 1;
						((WorldScheduler) world).schedule(ticks, () -> {
							if (caster != null && caster.isAlive()) {
								fallProjectile(world, caster, target, targetLocation, spellEntry, context, nextSequenceIndex);
							}
						});
					}
				}

				return true;
			}
		}
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate direct damage, direct healing and damage type override
	 */
	@Overwrite
	private static boolean performImpact(World world, LivingEntity caster, Entity target, RegistryEntry<Spell> spellEntry, Spell.Impact impact, SpellHelper.ImpactContext context, Collection<ServerPlayerEntity> trackers) {
		if (!((Entity) target).isAttackable()) {
			return false;
		} else {
			boolean success = false;
			boolean critical = false;
			boolean isKnockbackPushed = false;
			Spell spell = (Spell) spellEntry.value();

			try {
				if (impact.action.apply_to_caster) {
					target = caster;
				} else {
					SpellTarget.Intent intent = SpellHelper.impactIntent(impact.action);
					if (!EntityRelations.actionAllowed(context.focusMode(), intent, caster, (Entity) target)) {
						return false;
					}

					if (intent == SpellTarget.Intent.HARMFUL && context.focusMode() == SpellTarget.FocusMode.AREA && ((EntityImmunity) target).isImmuneTo(net.spell_engine.api.effect.EntityImmunity.Type.AREA_EFFECT)) {
						return false;
					}
				}

				SpellHelper.TargetConditionResult conditionResult = SpellHelper.evaluateImpactConditions((Entity) target, caster, impact.target_modifiers);
				if (!conditionResult.allowed()) {
					return false;
				}

				double particleMultiplier = (double) (1.0F * context.total());
				SpellPower.Result power = context.power();
				SpellSchool school = impact.school != null ? impact.school : spell.school;
				if (power == null || power.school() != school) {
					power = SpellPower.getSpellPower(school, caster);
				}

				if (impact.attribute != null) {
					RegistryEntry.Reference<EntityAttribute> attributeOverride = (RegistryEntry.Reference)Registries.ATTRIBUTE.getEntry(Identifier.of(impact.attribute)).get();
					double value = caster.getAttributeValue(attributeOverride);
					power = new SpellPower.Result(power.school(), value, power.criticalChance(), power.criticalDamage());
				}

				float bonusPower = 1.0F + (Float) conditionResult.modifiers().stream().map((modifier) -> {
					return modifier.power_multiplier;
				}).reduce(0.0F, Float::sum);
				Float bonusCritChance = (Float) conditionResult.modifiers().stream().map((modifier) -> {
					return modifier.critical_chance_bonus;
				}).reduce(0.0F, Float::sum);
				Float bonusCritDamage = (Float) conditionResult.modifiers().stream().map((modifier) -> {
					return modifier.critical_damage_bonus;
				}).reduce(0.0F, Float::sum);
				power = new SpellPower.Result(power.school(), power.baseValue() * (double) bonusPower, power.criticalChance() + (double) bonusCritChance, power.criticalDamage() + (double) bonusCritDamage);
				if (power.baseValue() < (double) impact.action.min_power || power.baseValue() > (double)impact.action.max_power) {
					double clampedValue = MathHelper.clamp(power.baseValue(), (double)impact.action.min_power, (double)impact.action.max_power);
					power = new SpellPower.Result(power.school(), clampedValue, power.criticalChance(), power.criticalDamage());
				}

				Vec3d groundJustBelow;
				LivingEntity livingTarget;
				Identifier id;
				label266:
				switch (impact.action.type) {
					case DAMAGE:
						Spell.Impact.Action.Damage damageData = impact.action.damage;
						float knockbackMultiplier = Math.max(0.0F, damageData.knockback * context.total());
						SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;
						int timeUntilRegen = ((Entity) target).timeUntilRegen;
						if (target instanceof LivingEntity) {
							LivingEntity livingEntity = (LivingEntity) target;
							((ConfigurableKnockback) livingEntity).pushKnockbackMultiplier_SpellEngine(context.hasOffset() ? 0.0F : knockbackMultiplier);
							isKnockbackPushed = true;
							if (damageData.bypass_iframes && SpellEngineMod.config.bypass_iframes) {
								((Entity) target).timeUntilRegen = 0;
							}

							vulnerability = SpellPower.getVulnerability(livingEntity, school);
						}

						SpellPower.Result.Value result = power.random(vulnerability);
						critical = result.isCritical();
						double amount = result.amount();
						amount *= (double) damageData.spell_power_coefficient;
						amount *= (double) context.total();
						if (context.isChanneled()) {
							amount *= (double) SpellPower.getHaste(caster, school);
						}

						// direct damage
						double directDamageAmount = ((DuckSpellImpactActionDamageMixin) damageData).betteradventuremode$getDirectDamage();
						if (directDamageAmount > 0.0) {
							amount = directDamageAmount;
						}

						particleMultiplier = power.criticalDamage() + (double) vulnerability.criticalDamageBonus();
						caster.onAttacking((Entity) target);

						// damage type override
						DamageSource damageSource = null;
						String damageTypeOverride = ((DuckSpellImpactActionDamageMixin) damageData).betteradventuremode$getDamageTypeOverride();
						if (!damageTypeOverride.isEmpty()) {
							Identifier damageTypeOverrideId = Identifier.tryParse(damageTypeOverride);
							if (damageTypeOverrideId != null) {
								RegistryKey<DamageType> key = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, damageTypeOverrideId);
								Registry<DamageType> registry = ((DamageSourcesAccessor) caster.getDamageSources()).getRegistry();
								damageSource = new DamageSource(registry.entryOf(key), caster);
							}
						}
						if (damageSource == null) {
							damageSource = SpellDamageSource.create(school, caster);
						}
						((Entity) target).damage(damageSource, (float) amount);

						if (target instanceof LivingEntity) {
							LivingEntity livingEntity = (LivingEntity) target;
							((ConfigurableKnockback) livingEntity).popKnockbackMultiplier_SpellEngine();
							isKnockbackPushed = false;
							((Entity) target).timeUntilRegen = timeUntilRegen;
							if (context.hasOffset()) {
								groundJustBelow = context.knockbackDirection(livingEntity.getPos()).negate();
								livingEntity.takeKnockback((double) (0.4F * knockbackMultiplier), groundJustBelow.x, groundJustBelow.z);
							}
						}

						success = true;
						break;
					case HEAL:
						if (target instanceof LivingEntity) {
							livingTarget = (LivingEntity) target;
							Spell.Impact.Action.Heal healData = impact.action.heal;
							particleMultiplier = power.criticalDamage();
							SpellPower.Result.Value healResult = power.random();
							critical = healResult.isCritical();
							double healAmount = healResult.amount();
							healAmount *= (double) healData.spell_power_coefficient;
							healAmount *= (double) context.total();

							// direct heal
							double directHealAmount = ((DuckSpellImpactActionHealMixin) healData).betteradventuremode$getDirectHeal();
							if (directHealAmount > 0) {
								healAmount = directHealAmount;
							}

							if (context.isChanneled()) {
								healAmount *= (double) SpellPower.getHaste(caster, school);
							}
							livingTarget.heal((float) healAmount);
							success = true;
						}
						break;
					case STATUS_EFFECT:
						Spell.Impact.Action.StatusEffect data = impact.action.status_effect;
						if (!(target instanceof LivingEntity)) {
							break;
						}

						livingTarget = (LivingEntity) target;
							Optional<RegistryEntry<StatusEffect>> optionalEffect = Optional.empty();
							if (data.remove != null) {
								List<StatusEffectInstance> effects = livingTarget.getStatusEffects().stream().filter((instance) -> {
									return ((StatusEffect)instance.getEffectType().value()).isBeneficial() == data.remove.select_beneficial;
								}).toList();
								switch (data.remove.selector) {
									case RANDOM -> optionalEffect = Optional.of((StatusEffectInstance)effects.get(world.random.nextInt(effects.size()))).map(StatusEffectInstance::getEffectType);
									case FIRST -> optionalEffect = Optional.of((StatusEffectInstance)effects.getFirst()).map(StatusEffectInstance::getEffectType);
								}
							} else {
								id = Identifier.of(data.effect_id);
								optionalEffect = Optional.of((RegistryEntry)Registries.STATUS_EFFECT.getEntry(id).get());
							}

							if (optionalEffect.isEmpty()) {
								return false;
							}

							RegistryEntry<StatusEffect> effect = (RegistryEntry) optionalEffect.get();
							if (!SpellHelper.underApplyLimit(power, livingTarget, school, data.apply_limit)) {
								return false;
							}

							int amplifier = data.amplifier + (int) ((double) data.amplifier_power_multiplier * power.nonCriticalValue());
							switch (data.apply_mode) {
								case ADD:
								case SET:
									if (!((Entity)target).getType().isIn(SpellEngineEntityTags.bosses) || !StatusEffectClassification.isMovementImpairing(effect) && !StatusEffectClassification.disablesMobAI(effect)) {
									int duration = Math.round(data.duration * 20.0F);
									boolean showParticles = data.show_particles;
									StatusEffectInstance currentEffect;
									if (data.apply_mode == Spell.Impact.Action.StatusEffect.ApplyMode.ADD) {
										currentEffect = livingTarget.getStatusEffect(effect);
										int newAmplifier = 0;
										if (currentEffect != null) {
											int currentAmplifier = currentEffect.getAmplifier();
											int incrementedAmplifier = currentAmplifier + 1;
											newAmplifier = Math.min(incrementedAmplifier, amplifier);
											if (!data.refresh_duration) {
												if (currentAmplifier == newAmplifier) {
													return false;
												}

												duration = currentEffect.getDuration();
											}
										}

										amplifier = newAmplifier;
									}

									currentEffect = new StatusEffectInstance(effect, duration, amplifier, false, showParticles, true);
									livingTarget.addStatusEffect(currentEffect, caster);
									success = true;
										break label266;
									}

									return false;
								case REMOVE:
									if (livingTarget.hasStatusEffect(effect)) {
										StatusEffectInstance currentEffect = livingTarget.getStatusEffect(effect);
										int newAmplifier = amplifier > 0 ? currentEffect.getAmplifier() - amplifier : -1;
										if (newAmplifier < 0) {
											livingTarget.removeStatusEffect(effect);
										} else {
											livingTarget.addStatusEffect(new StatusEffectInstance(effect, currentEffect.getDuration(), newAmplifier, currentEffect.isAmbient(), currentEffect.shouldShowParticles(), currentEffect.shouldShowIcon()), caster);
										}

										success = true;
									}
								default:
									break label266;
							}
					case FIRE:
						Spell.Impact.Action.Fire fireData = impact.action.fire;
						((Entity) target).setOnFireFor(fireData.duration);
						if (((Entity) target).getFireTicks() > 0) {
							((Entity) target).setFireTicks(((Entity) target).getFireTicks() + fireData.tick_offset);
						}
						break;
					case SPAWN:
						List<Spell.Impact.Action.Spawn> spawns = impact.action.spawns;
						if (spawns != null && !spawns.isEmpty()) {
							Iterator var44 = spawns.iterator();

							while (true) {
								if (!var44.hasNext()) {
									break label266;
								}

								Spell.Impact.Action.Spawn spawnData = (Spell.Impact.Action.Spawn) var44.next();
								Identifier entity_type_id = Identifier.of(spawnData.entity_type_id);
								EntityType<?> type = (EntityType) Registries.ENTITY_TYPE.get(entity_type_id);
								Entity entity = type.create(world);
								SpellHelper.applyEntityPlacement(entity, caster, ((Entity) target).getPos(), spawnData.placement);
								if (entity instanceof SpellEntity.Spawned) {
									SpellEntity.Spawned spellSpawnedEntity = (SpellEntity.Spawned)entity;
									SpellEntity.Spawned.Args args = new SpellEntity.Spawned.Args(caster, spellEntry, spawnData, context);
									spellSpawnedEntity.onSpawnedBySpell(args);
								}

								((WorldScheduler) world).schedule(spawnData.delay_ticks, () -> {
									world.spawnEntity(entity);
								});
								success = true;
							}
						}

						return false;
					case TELEPORT:
						Spell.Impact.Action.Teleport teleportData = impact.action.teleport;
						if (!(target instanceof LivingEntity)) {
							break;
						}

						livingTarget = (LivingEntity) target;
						LivingEntity teleportedEntity = null;
						Vec3d destination = null;
						Vec3d startingPosition = null;
						Float applyRotation = null;
						Vec3d look;
						switch (teleportData.mode) {
							case FORWARD:
								teleportedEntity = livingTarget;
								Spell.Impact.Action.Teleport.Forward forward = teleportData.forward;
								look = ((Entity) target).getRotationVector();
								startingPosition = ((Entity) target).getPos();
								destination = TargetHelper.findTeleportDestination(teleportedEntity, look, forward.distance, teleportData.required_clearance_block_y);
								groundJustBelow = TargetHelper.findSolidBlockBelow(teleportedEntity, destination, ((Entity) target).getWorld(), -1.5F);
								if (groundJustBelow != null) {
									destination = groundJustBelow;
								}
								break;
							case BEHIND_TARGET:
								if (livingTarget == caster) {
									return false;
								}

								look = ((Entity) target).getRotationVector();
								float distance = 1.0F;
								if (teleportData.behind_target != null) {
									distance = teleportData.behind_target.distance;
								}

								teleportedEntity = caster;
								startingPosition = caster.getPos();
								destination = ((Entity) target).getPos().add(look.multiply((double) (-distance)));
								groundJustBelow = TargetHelper.findSolidBlockBelow(teleportedEntity, destination, ((Entity) target).getWorld(), -1.5F);
								if (groundJustBelow != null) {
									destination = groundJustBelow;
								}

								double x = look.x;
								double z = look.z;
								float yaw = (float) Math.toDegrees(Math.atan2(-x, z));
								yaw = yaw < 0.0F ? yaw + 360.0F : yaw;
								applyRotation = yaw;
						}

						if (destination == null || startingPosition == null || teleportedEntity == null) {
							break;
						}

						label251: {
							ParticleHelper.sendBatches(teleportedEntity, teleportData.depart_particles, false);
							world.emitGameEvent(GameEvent.TELEPORT, startingPosition, GameEvent.Emitter.of(teleportedEntity));
							if (applyRotation != null && teleportedEntity instanceof ServerPlayerEntity) {
								ServerPlayerEntity serverPlayer = (ServerPlayerEntity) teleportedEntity;
								if (world instanceof ServerWorld) {
									ServerWorld serverWorld = (ServerWorld) world;
									serverPlayer.teleport(serverWorld, destination.x, destination.y, destination.z, applyRotation, serverPlayer.getPitch());
									break label251;
								}
							}

							teleportedEntity.teleport(destination.x, destination.y, destination.z, false);
						}

						success = true;
						ParticleHelper.sendBatches(teleportedEntity, teleportData.arrive_particles, false);
						break;
					case CUSTOM:
						if (impact.action.custom != null) {
							SpellHandlers.CustomImpact handler = (SpellHandlers.CustomImpact) SpellHandlers.customImpact.get(impact.action.custom.handler);
							if (handler != null) {
								SpellHandlers.ImpactResult custom_result = handler.onSpellImpact(spellEntry, power, caster, (Entity)target, context);
								particleMultiplier = power.criticalDamage();
								success = custom_result.success();
								critical = custom_result.critical();
							}
						}
				}

				if (success) {
					if (impact.particles != null) {
						float countMultiplier = critical ? (float)particleMultiplier : 1.0F;
						ParticleHelper.sendBatches((Entity) target, impact.particles, countMultiplier * caster.getScale(), trackers);
					}

					if (impact.sound != null) {
						SoundHelper.playSound(world, (Entity) target, impact.sound);
					}

                    SpellTriggers.onSpellImpactSpecific((PlayerEntity)caster, (Entity)target, spellEntry, impact, critical);
				}
            } catch (Exception var33) {
                Exception e = var33;
				System.err.println("Failed to perform impact effect");
                System.err.println(e.getMessage());
				if (isKnockbackPushed) {
					((ConfigurableKnockback) target).popKnockbackMultiplier_SpellEngine();
				}
			}

			return success;
		}
	}
}
