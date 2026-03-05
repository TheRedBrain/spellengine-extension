package com.github.theredbrain.spellengineextension.enchantment;

import com.github.theredbrain.spellengineextension.server.network.DuckServerPlayerEntityMixin;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public record ProvideSpellsEnchantmentEntityEffect(List<String> providedSpellsList) implements EnchantmentEntityEffect {
	public static final MapCodec<ProvideSpellsEnchantmentEntityEffect> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(Codec.STRING.listOf().fieldOf("provided_spells_list").forGetter(ProvideSpellsEnchantmentEntityEffect::providedSpellsList)).apply(instance, ProvideSpellsEnchantmentEntityEffect::new)
	);

	@Override
	public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			((DuckServerPlayerEntityMixin) serverPlayerEntity).spellengineextension$addEnchantmentProvidedSpells(this.providedSpellsList);
		}
	}

	@Override
	public MapCodec<ProvideSpellsEnchantmentEntityEffect> getCodec() {
		return CODEC;
	}
}
