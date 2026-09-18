package com.github.theredbrain.spellengineextension.registry;

import com.github.theredbrain.spellengineextension.spell_engine.ExtendedSpellHelper;
import net.spell_engine.api.spell.event.SpellEvents;

public class ServerEventRegistry {

	public static void init() {

		SpellEvents.CASTING_ATTEMPT.PRE.register((args) ->
				ExtendedSpellHelper.checkForCustomSpellCost(args.caster(), args.spell())
		);

		SpellEvents.CASTING_ATTEMPT.POST.register((args) ->
				ExtendedSpellHelper.consumeCustomSpellCost(args.caster(), args.spell(), args.itemStack())
		);

		SpellEvents.COST_CONSUME.register((args) ->
				ExtendedSpellHelper.consumeCustomAfterCastingSpellCost(args.caster(), args.spell(), args.itemStack())
		);

		SpellEvents.SPELL_CAST.register((args) ->
				ExtendedSpellHelper.applyAfterCastingMovementLockingTicks(args.caster(), args.spell())
		);

	}
}
