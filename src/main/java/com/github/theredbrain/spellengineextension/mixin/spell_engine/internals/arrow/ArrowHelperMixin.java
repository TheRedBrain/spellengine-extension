package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals.arrow;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellLaunchPropertiesMixin;
import net.fabric_extras.ranged_weapon.api.EntityAttributes_RangedWeapon;
import net.fabric_extras.ranged_weapon.internal.ScalingUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellEvents;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.WorldScheduler;
import net.spell_engine.internals.arrow.ArrowHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArrowHelper.class)
@SuppressWarnings("UnreachableCode")
public abstract class ArrowHelperMixin {

	@Shadow
	static ProjectileEntity shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated, SpellInfo spellInfo) {
		throw new AssertionError();
	}

	/**
	 * @author TheRedBrain
	 * @reason integrate launch properties entity attributes
	 */
	@Overwrite
	public static void shootArrow(World world, LivingEntity shooter, SpellInfo spellInfo, SpellHelper.ImpactContext context, int sequenceIndex) {
		boolean isCreative = shooter instanceof PlayerEntity && ((PlayerEntity) shooter).getAbilities().creativeMode;
		Spell spell = spellInfo.spell();
		Spell.Release.Target.ShootArrow shoot_arrow = spell.release.target.shoot_arrow;
		if (shoot_arrow != null) {
			Spell.LaunchProperties launchProperties = shoot_arrow.launch_properties.copy();

			// region modifying mutable launch properties
			ServerConfig serverConfig = SpellEngineExtension.SERVER_CONFIG;
			if (serverConfig.spell_launch_properties_extra_launch_count_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$respectExtraLaunchCountAttribute()) {
				launchProperties.extra_launch_count += (int) (((DuckLivingEntityMixin) shooter).spellengineextension$getExtraLaunchCount());
			}
			if (serverConfig.spell_launch_properties_extra_launch_delay_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$respectExtraLaunchDelayAttribute()) {
				launchProperties.extra_launch_delay += (int) (((DuckLivingEntityMixin) shooter).spellengineextension$getExtraLaunchDelay());
			}
			if (serverConfig.spell_launch_properties_extra_velocity_attribute_allowed.get() && ((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$respectExtraVelocityAttribute()) {
				launchProperties.velocity += (int) (((DuckLivingEntityMixin) shooter).spellengineextension$getExtraVelocity());
			}
			// endregion modifying mutable launch properties

			PlayerEntity player = (PlayerEntity) shooter;
			boolean infinity = !shoot_arrow.consume_arrow || player.isCreative() || EnchantmentHelper.getLevel(Enchantments.INFINITY, player.getMainHandStack()) > 0;
			PersistentProjectileEntity.PickupPermission arrowPickUpType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
			ItemStack ammo = new ItemStack(Items.ARROW);
			if (!infinity) {
				arrowPickUpType = PersistentProjectileEntity.PickupPermission.ALLOWED;
				if (!ArrowHelper.tryConsumeItem(player, Items.ARROW)) {
					return;
				}
			}

			ScalingUtil.Scaling scaling = ScalingUtil.scaling(shooter.getMainHandStack(), shooter.getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.attribute));
			ProjectileEntity projectile = shoot(world, shooter, Hand.MAIN_HAND, shooter.getMainHandStack(), ammo, 1.0F, isCreative, (float) ((double) launchProperties.velocity * scaling.velocity()), 1.0F, 0.0F, spellInfo);
			if (projectile instanceof PersistentProjectileEntity) {
				PersistentProjectileEntity persistentProjectile = (PersistentProjectileEntity) projectile;
				persistentProjectile.setDamage(persistentProjectile.getDamage() * (double) shoot_arrow.damage_multiplier * scaling.damage());
				persistentProjectile.pickupType = arrowPickUpType;
			}

			if (SpellEvents.ARROW_FIRED.isListened()) {
				SpellEvents.ARROW_FIRED.invoke((listener) -> {
					listener.onArrowLaunch(new SpellEvents.ArrowLaunchEvent(projectile, shooter, spellInfo, context, sequenceIndex));
				});
			}

			int extra_launch = launchProperties.extra_launch_count;
			if (sequenceIndex == 0 && extra_launch > 0) {
				for (int i = 0; i < extra_launch; ++i) {
					int ticks = (i + 1) * launchProperties.extra_launch_delay;
					int nextSequenceIndex = i + 1;
					((WorldScheduler) world).schedule(ticks, () -> {
						if (shooter != null && shooter.isAlive()) {
							shootArrow(world, shooter, spellInfo, context, nextSequenceIndex);
						}
					});
				}
			}
		}
	}
}