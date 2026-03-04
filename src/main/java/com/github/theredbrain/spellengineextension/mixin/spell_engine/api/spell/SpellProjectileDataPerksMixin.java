package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellProjectileDataPerksMixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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

	@WrapMethod(method = "copy()Lnet/spell_engine/api/spell/Spell$ProjectileData$Perks;", remap = false)
	public Spell.ProjectileData.Perks spellengineextension$wrap_copy(Operation<Spell.ProjectileData.Perks> original) {
		Spell.ProjectileData.Perks perks = original.call();
		((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$setRespectExtraRicochetAttribute(this.respect_extra_ricochet_attribute);
		((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$setRespectExtraRicochetRangeAttribute(this.respect_extra_ricochet_range_attribute);
		((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$setRespectExtraBounceAttribute(this.respect_extra_bounce_attribute);
		((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$setRespectExtraPierceAttribute(this.respect_extra_pierce_attribute);
		((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$setRespectExtraChainReactionSizeAttribute(this.respect_extra_chain_reaction_size_attribute);
		((DuckSpellProjectileDataPerksMixin) perks).spellengineextension$setRespectExtraChainReactionTriggersAttribute(this.respect_extra_chain_reaction_triggers_attribute);
		return perks;
	}
}
