package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.spell_engine.ExtendedSpellHelper;
import net.spell_engine.api.spell.event.SpellEvents;

public class ServerEventRegistry {

	public static void init() {

		SpellEvents.CASTING_ATTEMPT.POST.register((args) -> {
			return ExtendedSpellHelper.checkForCustomSpellCost(args.caster(), args.spell());
		});

		SpellEvents.COST_CONSUME.register((args) -> {
			ExtendedSpellHelper.consumeCustomSpellCost(args.caster(), args.spell(), args.itemStack());
		});

		SpellEvents.SPELL_CAST.register((args) -> {
			ExtendedSpellHelper.applyAfterCastingMovementLockingTicks(args.caster(), args.spell());
		});

	}
}
