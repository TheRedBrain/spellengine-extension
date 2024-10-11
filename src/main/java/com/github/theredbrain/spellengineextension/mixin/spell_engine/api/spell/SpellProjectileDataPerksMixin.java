package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellProjectileDataPerksMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Spell.ProjectileData.Perks.class)
public class SpellProjectileDataPerksMixin implements DuckSpellProjectileDataPerksMixin {
	@Unique
	private boolean respect_extra_ricochet_attribute = true;
	@Unique
	private boolean respect_extra_ricochet_range_attribute = true;
	@Unique
	private boolean respect_extra_bounce_attribute = true;
	@Unique
	private boolean respect_extra_pierce_attribute = true;
	@Unique
	private boolean respect_extra_chain_reaction_size_attribute = true;
	@Unique
	private boolean respect_extra_chain_reaction_triggers_attribute = true;

	@Override
	public boolean spellengineextension$respectExtraRicochetAttribute() {
		return this.respect_extra_ricochet_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraRicochetAttribute(boolean respect_extra_ricochet_attribute) {
		this.respect_extra_ricochet_attribute = respect_extra_ricochet_attribute;
	}

	@Override
	public boolean spellengineextension$respectExtraRicochetRangeAttribute() {
		return this.respect_extra_ricochet_range_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraRicochetRangeAttribute(boolean respect_extra_ricochet_range_attribute) {
		this.respect_extra_ricochet_range_attribute = respect_extra_ricochet_range_attribute;
	}

	@Override
	public boolean spellengineextension$respectExtraBounceAttribute() {
		return this.respect_extra_bounce_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraBounceAttribute(boolean respect_extra_bounce_attribute) {
		this.respect_extra_bounce_attribute = respect_extra_bounce_attribute;
	}

	@Override
	public boolean spellengineextension$respectExtraPierceAttribute() {
		return this.respect_extra_pierce_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraPierceAttribute(boolean respect_extra_pierce_attribute) {
		this.respect_extra_pierce_attribute = respect_extra_pierce_attribute;
	}

	@Override
	public boolean spellengineextension$respectExtraChainReactionSizeAttribute() {
		return this.respect_extra_chain_reaction_size_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraChainReactionSizeAttribute(boolean respect_extra_chain_reaction_size_attribute) {
		this.respect_extra_chain_reaction_size_attribute = respect_extra_chain_reaction_size_attribute;
	}

	@Override
	public boolean spellengineextension$respectExtraChainReactionTriggersAttribute() {
		return this.respect_extra_chain_reaction_triggers_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraChainReactionTriggersAttribute(boolean respect_extra_chain_reaction_triggers_attribute) {
		this.respect_extra_chain_reaction_triggers_attribute = respect_extra_chain_reaction_triggers_attribute;
	}

	@Inject(method = "copy", at = @At("RETURN"), cancellable = true, remap = false)
	public void copy(CallbackInfoReturnable<Spell.ProjectileData.Perks> cir) {
		Spell.ProjectileData.Perks perks = cir.getReturnValue();
		((DuckSpellProjectileDataPerksMixin)perks).spellengineextension$setRespectExtraRicochetAttribute(this.respect_extra_ricochet_attribute);
		((DuckSpellProjectileDataPerksMixin)perks).spellengineextension$setRespectExtraRicochetRangeAttribute(this.respect_extra_ricochet_range_attribute);
		((DuckSpellProjectileDataPerksMixin)perks).spellengineextension$setRespectExtraBounceAttribute(this.respect_extra_bounce_attribute);
		((DuckSpellProjectileDataPerksMixin)perks).spellengineextension$setRespectExtraPierceAttribute(this.respect_extra_pierce_attribute);
		((DuckSpellProjectileDataPerksMixin)perks).spellengineextension$setRespectExtraChainReactionSizeAttribute(this.respect_extra_chain_reaction_size_attribute);
		((DuckSpellProjectileDataPerksMixin)perks).spellengineextension$setRespectExtraChainReactionTriggersAttribute(this.respect_extra_chain_reaction_triggers_attribute);
		cir.setReturnValue(perks);
	}
}
