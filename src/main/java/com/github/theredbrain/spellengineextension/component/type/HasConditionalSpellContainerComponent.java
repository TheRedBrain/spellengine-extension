package com.github.theredbrain.spellengineextension.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record HasConditionalSpellContainerComponent(
		boolean is_main_hand_valid,
		boolean is_off_hand_valid,
		boolean is_two_handed_valid,
		boolean is_dual_wielding_valid,
		String dual_wielding_tag,
		boolean prevent_casting_of_all_spells,
		List<String> fall_back_spell_ids,
		boolean is_valid
		) {
	public static final Codec<HasConditionalSpellContainerComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.BOOL.optionalFieldOf("is_main_hand_valid", true).forGetter(HasConditionalSpellContainerComponent::is_main_hand_valid),
							Codec.BOOL.optionalFieldOf("is_off_hand_valid", true).forGetter(HasConditionalSpellContainerComponent::is_off_hand_valid),
							Codec.BOOL.optionalFieldOf("is_two_handed_valid", false).forGetter(HasConditionalSpellContainerComponent::is_two_handed_valid),
							Codec.BOOL.optionalFieldOf("is_dual_wielding_valid", false).forGetter(HasConditionalSpellContainerComponent::is_dual_wielding_valid),
							Codec.STRING.optionalFieldOf("dual_wielding_tag", "").forGetter(HasConditionalSpellContainerComponent::dual_wielding_tag),
							Codec.BOOL.optionalFieldOf("prevent_casting_of_all_spells", false).forGetter(HasConditionalSpellContainerComponent::prevent_casting_of_all_spells),
							Codec.STRING.listOf().optionalFieldOf("fall_back_spell_ids", List.of()).forGetter(HasConditionalSpellContainerComponent::fall_back_spell_ids),
							Codec.BOOL.optionalFieldOf("is_valid", true).forGetter(HasConditionalSpellContainerComponent::is_valid)
							)
					.apply(instance, HasConditionalSpellContainerComponent::new)
	);
//	public static final PacketCodec<RegistryByteBuf, HasConditionalSpellContainerComponent> PACKET_CODEC = PacketCodec.tuple(
//			PacketCodecs.BOOL,
//			HasConditionalSpellContainerComponent::is_main_hand_valid,
//			PacketCodecs.BOOL,
//			HasConditionalSpellContainerComponent::is_off_hand_valid,
//			PacketCodecs.BOOL,
//			HasConditionalSpellContainerComponent::is_two_handed_valid,
//			PacketCodecs.BOOL,
//			HasConditionalSpellContainerComponent::is_dual_wielding_valid,
//			PacketCodecs.STRING,
//			HasConditionalSpellContainerComponent::dual_wielding_tag,
//			PacketCodecs.BOOL,
//			HasConditionalSpellContainerComponent::is_valid,
//			HasConditionalSpellContainerComponent::new
//	);

	public HasConditionalSpellContainerComponent(
			boolean is_main_hand_valid,
			boolean is_off_hand_valid,
			boolean is_two_handed_valid,
			boolean is_dual_wielding_valid,
			String dual_wielding_tag,
			boolean prevent_casting_of_all_spells,
			List<String> fall_back_spell_ids,
			boolean is_valid
			) {
		this.is_main_hand_valid = is_main_hand_valid;
		this.is_off_hand_valid = is_off_hand_valid;
		this.is_two_handed_valid = is_two_handed_valid;
		this.is_dual_wielding_valid = is_dual_wielding_valid;
		this.dual_wielding_tag = dual_wielding_tag != null ? dual_wielding_tag : "";
		this.prevent_casting_of_all_spells = prevent_casting_of_all_spells;
		this.fall_back_spell_ids = fall_back_spell_ids != null ? fall_back_spell_ids : List.of();
		this.is_valid = is_valid;
	}

}
