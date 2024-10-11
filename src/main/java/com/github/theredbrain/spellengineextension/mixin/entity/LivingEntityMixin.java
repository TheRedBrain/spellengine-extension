package com.github.theredbrain.spellengineextension.mixin.entity;

import com.github.theredbrain.spellengineextension.SpellEngineExtension;
import com.github.theredbrain.spellengineextension.entity.DuckLivingEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DuckLivingEntityMixin {

	@Shadow
	public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "createLivingAttributes", at = @At("RETURN"))
	private static void spellengineextension$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue()
				.add(SpellEngineExtension.HEALTH_SPELL_COST_MULTIPLIER)
				.add(SpellEngineExtension.MANA_SPELL_COST_MULTIPLIER)
				.add(SpellEngineExtension.STAMINA_SPELL_COST_MULTIPLIER)
				.add(SpellEngineExtension.EXTRA_LAUNCH_COUNT)
				.add(SpellEngineExtension.EXTRA_LAUNCH_DELAY)
				.add(SpellEngineExtension.EXTRA_VELOCITY)
				.add(SpellEngineExtension.EXTRA_RICOCHET)
				.add(SpellEngineExtension.EXTRA_RICOCHET_RANGE)
				.add(SpellEngineExtension.EXTRA_BOUNCE)
				.add(SpellEngineExtension.EXTRA_PIERCE)
				.add(SpellEngineExtension.EXTRA_CHAIN_REACTION_SIZE)
				.add(SpellEngineExtension.EXTRA_CHAIN_REACTION_TRIGGERS)
		;
	}

	@Override
	public float spellengineextension$getHealthSpellCostMultiplier() {
		return (float) this.getAttributeValue(SpellEngineExtension.HEALTH_SPELL_COST_MULTIPLIER);
	}

	@Override
	public float spellengineextension$getManaSpellCostMultiplier() {
		return (float) this.getAttributeValue(SpellEngineExtension.MANA_SPELL_COST_MULTIPLIER);
	}

	@Override
	public float spellengineextension$getStaminaSpellCostMultiplier() {
		return (float) this.getAttributeValue(SpellEngineExtension.STAMINA_SPELL_COST_MULTIPLIER);
	}

	@Override
	public float spellengineextension$getExtraLaunchCount() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_LAUNCH_COUNT);
	}

	@Override
	public float spellengineextension$getExtraLaunchDelay() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_LAUNCH_DELAY);
	}

	@Override
	public float spellengineextension$getExtraVelocity() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_VELOCITY);
	}

	@Override
	public float spellengineextension$getExtraRicochet() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_RICOCHET);
	}

	@Override
	public float spellengineextension$getExtraRicochetRange() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_RICOCHET_RANGE);
	}

	@Override
	public float spellengineextension$getExtraBounce() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_BOUNCE);
	}

	@Override
	public float spellengineextension$getExtraPierce() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_PIERCE);
	}

	@Override
	public float spellengineextension$getExtraChainReactionSize() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_CHAIN_REACTION_SIZE);
	}

	@Override
	public float spellengineextension$getExtraChainReactionTriggers() {
		return (float) this.getAttributeValue(SpellEngineExtension.EXTRA_CHAIN_REACTION_TRIGGERS);
	}
}
