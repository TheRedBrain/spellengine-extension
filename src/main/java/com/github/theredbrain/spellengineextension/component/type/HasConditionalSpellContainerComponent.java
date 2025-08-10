package com.github.theredbrain.spellengineextension.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record HasConditionalSpellContainerComponent(
		boolean is_main_hand_valid,
		boolean is_off_hand_valid,
		boolean is_valid
) {
	public static final Codec<HasConditionalSpellContainerComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.BOOL.fieldOf("is_main_hand_valid").forGetter(HasConditionalSpellContainerComponent::is_main_hand_valid),
							Codec.BOOL.fieldOf("is_off_hand_valid").forGetter(HasConditionalSpellContainerComponent::is_off_hand_valid),
							Codec.BOOL.fieldOf("is_valid").forGetter(HasConditionalSpellContainerComponent::is_valid)
					)
					.apply(instance, HasConditionalSpellContainerComponent::new)
	);
	public static final PacketCodec<RegistryByteBuf, HasConditionalSpellContainerComponent> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.BOOL,
			HasConditionalSpellContainerComponent::is_main_hand_valid,
			PacketCodecs.BOOL,
			HasConditionalSpellContainerComponent::is_off_hand_valid,
			PacketCodecs.BOOL,
			HasConditionalSpellContainerComponent::is_valid,
			HasConditionalSpellContainerComponent::new
	);
}
