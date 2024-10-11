package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellLaunchPropertiesMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Spell.LaunchProperties.class)
public class SpellLaunchPropertiesMixin implements DuckSpellLaunchPropertiesMixin {
	@Unique
	private boolean respect_extra_launch_count_attribute = true;
	@Unique
	private boolean respect_extra_launch_delay_attribute = true;
	@Unique
	private boolean respect_extra_velocity_attribute = true;

	@Override
	public boolean spellengineextension$respectExtraLaunchCountAttribute() {
		return this.respect_extra_launch_count_attribute;
	}

	@Override
	public boolean spellengineextension$respectExtraLaunchDelayAttribute() {
		return this.respect_extra_launch_delay_attribute;
	}

	@Override
	public boolean spellengineextension$respectExtraVelocityAttribute() {
		return this.respect_extra_velocity_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraLaunchCountAttribute(boolean respect_extra_launch_count_attribute) {
		this.respect_extra_launch_count_attribute = respect_extra_launch_count_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraLaunchDelayAttribute(boolean respect_extra_launch_delay_attribute) {
		this.respect_extra_launch_delay_attribute = respect_extra_launch_delay_attribute;
	}

	@Override
	public void spellengineextension$setRespectExtraVelocityAttribute(boolean respect_extra_velocity_attribute) {
		this.respect_extra_velocity_attribute = respect_extra_velocity_attribute;
	}

	@Inject(method = "copy", at = @At("RETURN"), cancellable = true, remap = false)
	public void spellengineextension$copy(CallbackInfoReturnable<Spell.LaunchProperties> cir) {
		Spell.LaunchProperties launchProperties = cir.getReturnValue();
		((DuckSpellLaunchPropertiesMixin)launchProperties).spellengineextension$setRespectExtraLaunchCountAttribute(this.respect_extra_launch_count_attribute);
		((DuckSpellLaunchPropertiesMixin)launchProperties).spellengineextension$setRespectExtraLaunchDelayAttribute(this.respect_extra_launch_delay_attribute);
		((DuckSpellLaunchPropertiesMixin)launchProperties).spellengineextension$setRespectExtraVelocityAttribute(this.respect_extra_velocity_attribute);
		cir.setReturnValue(launchProperties);
	}
}
