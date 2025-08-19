package com.github.theredbrain.spellengineextension.mixin.spell_engine.api.spell;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellActiveCastMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Active.Cast.class)
public class SpellActiveCastMixin implements DuckSpellActiveCastMixin {

	@Unique
	private int after_casting_movement_locking_ticks = 0;

	@Override
	public int spellengineextension$getAfterCastingMovementLockingTicks() {
		return this.after_casting_movement_locking_ticks;
	}

	@Override
	public void spellengineextension$setAfterCastingMovementLockingTicks(int afterCastingMovementLockingTicks) {
		this.after_casting_movement_locking_ticks = afterCastingMovementLockingTicks;
	}
}
