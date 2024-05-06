package com.github.theredbrain.spellengineextension.mixin.spell_engine.internals;

import com.github.theredbrain.spellengineextension.spell_engine.DuckSpellCastAttemptMixin;
import net.spell_engine.internals.casting.SpellCast;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpellCast.Attempt.class)
public class SpellCastAttemptMixin implements DuckSpellCastAttemptMixin {

}
