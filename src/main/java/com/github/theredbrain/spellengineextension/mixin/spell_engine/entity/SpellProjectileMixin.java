package com.github.theredbrain.spellengineextension.mixin.spell_engine.entity;
//
//import com.github.theredbrain.spellengineextension.SpellEngineExtension;
//import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellDeliveryShootProjectileMixin;
//import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.projectile.ProjectileEntity;
//import net.minecraft.registry.entry.RegistryEntry;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.spell_engine.api.spell.Spell;
//import net.spell_engine.entity.SpellProjectile;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//
//@Mixin(SpellProjectile.class)
//public abstract class SpellProjectileMixin extends ProjectileEntity {
//
//	/* TODO gravity affected spell projectile */
//
//	@Shadow
//	private RegistryEntry<Spell> spellEntry;
//
//	public SpellProjectileMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
//		super(entityType, world);
//	}
//
//	// TODO on Spell Engine update
//	@WrapOperation(
//			method = "tick",
//			at = @At(value = "INVOKE", target = "Lnet/spell_engine/entity/SpellProjectile;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 3)
//	)
//	public Vec3d tick(SpellProjectile instance, Operation<Vec3d> original) {
//		float appliedGravity = this.spellEntry.value().deliver.projectile != null ? ((DuckSpellDeliveryShootProjectileMixin) this.spellEntry.value().deliver.projectile).spellengineextension$getAppliedGravity() : 0.0F;
//		SpellEngineExtension.LOGGER.info("applied gravity: " + appliedGravity);
//		if (appliedGravity != 0.0F) {
//			return original.call(instance).add(0.0, - appliedGravity, 0.0);
//		}
//		return original.call(instance);
//	}
//}
