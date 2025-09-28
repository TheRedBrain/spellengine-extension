package com.github.theredbrain.spellengineextension.compat;

import com.github.theredbrain.attackrangeattribute.entity.AttackRangeUsingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AttackRangeAttributeCompat {

	public static double getAttackRange(PlayerEntity playerEntity) {
		return ((AttackRangeUsingEntity) playerEntity).attackrangeattribute$getAttackRange();
	}

}
