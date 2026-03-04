package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellLaunchPropertiesMixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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

	@WrapMethod(method = "copy()Lnet/spell_engine/api/spell/Spell$LaunchProperties;", remap = false)
	public Spell.LaunchProperties spellengineextension$wrap_copy(Operation<Spell.LaunchProperties> original) {
		Spell.LaunchProperties launchProperties = original.call();
		((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$setRespectExtraLaunchCountAttribute(this.respect_extra_launch_count_attribute);
		((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$setRespectExtraLaunchDelayAttribute(this.respect_extra_launch_delay_attribute);
		((DuckSpellLaunchPropertiesMixin) launchProperties).spellengineextension$setRespectExtraVelocityAttribute(this.respect_extra_velocity_attribute);
		return launchProperties;
	}
}
