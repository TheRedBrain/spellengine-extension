package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals.arrow;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellLaunchPropertiesMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.WorldScheduler;
import net.spell_engine.internals.arrow.ArrowHelper;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.mixin.item.RangedWeaponAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(ArrowHelper.class)
@SuppressWarnings("UnreachableCode")
public class ArrowHelperMixin {

	/**
	 * @author TheRedBrain
	 * @reason integrate launch properties entity attributes
	 */
	@Overwrite
	public static void shootArrow(World world, LivingEntity shooter, SpellInfo spellInfo, SpellHelper.ImpactContext context, int sequenceIndex) {
		Spell spell = spellInfo.spell();
		Spell.Release.Target.ShootArrow shoot_arrow = spell.release.target.shoot_arrow;
		ItemStack weaponStack = shooter.getMainHandStack();
		Item weapon = Items.CROSSBOW;
		if (shoot_arrow != null && world instanceof ServerWorld serverWorld) {
			if (weapon instanceof RangedWeaponItem rangedWeapon) {
				Spell.LaunchProperties launchProperties = shoot_arrow.launch_properties.copy();

				ServerConfig serverConfig = SpellEngineExtension.serverConfig;
				if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed && ((DuckSpellLaunchPropertiesMixin)launchProperties).spellengineextension$respectExtraLaunchCountAttribute()) {
					launchProperties.extra_launch_count += (int) (((DuckLivingEntityMixin)shooter).spellengineextension$getExtraLaunchCount());
				}

				if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed && ((DuckSpellLaunchPropertiesMixin)launchProperties).spellengineextension$respectExtraLaunchDelayAttribute()) {
					launchProperties.extra_launch_delay += (int) (((DuckLivingEntityMixin)shooter).spellengineextension$getExtraLaunchDelay());
				}

				if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed && ((DuckSpellLaunchPropertiesMixin)launchProperties).spellengineextension$respectExtraVelocityAttribute()) {
					launchProperties.velocity += (int) (((DuckLivingEntityMixin)shooter).spellengineextension$getExtraVelocity());
				}

				ItemStack ammo;
				if (shooter instanceof PlayerEntity player) {
					ammo = player.getProjectileType(weaponStack);
				} else {
					ammo = new ItemStack(Items.ARROW);
				}

				List<ItemStack> loadedAmmo = RangedWeaponAccessor.load_SpellEngine(weaponStack, ammo, shooter);
				if (loadedAmmo.isEmpty()) {
					return;
				}

				if (shooter instanceof SpellCasterEntity caster) {
					caster.setTemporaryActiveSpell(spellInfo);
				}

				float divergence = sequenceIndex == 0 ? 0.0F : shoot_arrow.divergence;
				((RangedWeaponAccessor)rangedWeapon).shootAll_SpellEngine(serverWorld, shooter, Hand.MAIN_HAND, weaponStack, loadedAmmo, shoot_arrow.launch_properties.velocity, divergence, shoot_arrow.arrow_critical_strike, (LivingEntity)null);
				if (weapon instanceof BowItem) {
					world.playSound((PlayerEntity)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
				}

				if (shooter instanceof SpellCasterEntity caster) {
					caster.setTemporaryActiveSpell((SpellInfo)null);
				}

				int extra_launch = launchProperties.extra_launch_count;
				if (sequenceIndex == 0 && extra_launch > 0) {
					for(int i = 0; i < extra_launch; ++i) {
						int ticks = (i + 1) * launchProperties.extra_launch_delay;
						int nextSequenceIndex = i + 1;
						((WorldScheduler)world).schedule(ticks, () -> {
							if (shooter != null && shooter.isAlive()) {
								shootArrow(world, shooter, spellInfo, context, nextSequenceIndex);
							}
						});
					}
				}
			}
		}

	}
}
