package com.github.theredbrain.spellengineextension;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import com.github.theredbrain.spellengineextension.config.ServerConfig;
import com.github.theredbrain.spellengineextension.config.ServerConfigWrapper;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellEngineExtension implements ModInitializer {
	public static final String MOD_ID = "spellengineextension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	public static RegistryEntry<EntityAttribute> HEALTH_SPELL_COST_MULTIPLIER;
	public static RegistryEntry<EntityAttribute> MANA_SPELL_COST_MULTIPLIER;
	public static RegistryEntry<EntityAttribute> STAMINA_SPELL_COST_MULTIPLIER;

	public static RegistryEntry<EntityAttribute> EXTRA_LAUNCH_COUNT;

	public static RegistryEntry<EntityAttribute> EXTRA_RICOCHET;
	public static RegistryEntry<EntityAttribute> EXTRA_RICOCHET_RANGE;
	public static RegistryEntry<EntityAttribute> EXTRA_BOUNCE;
	public static RegistryEntry<EntityAttribute> EXTRA_PIERCE;
	public static RegistryEntry<EntityAttribute> EXTRA_CHAIN_REACTION_SIZE;
	public static RegistryEntry<EntityAttribute> EXTRA_CHAIN_REACTION_TRIGGERS;

	public static final boolean isManaAttributesLoaded = FabricLoader.getInstance().isModLoaded("manaattributes");
	public static final boolean isStaminaAttributesLoaded = FabricLoader.getInstance().isModLoaded("staminaattributes");

	public static float getCurrentMana(LivingEntity livingEntity) {
		float currentMana = 0.0F;
		if (isManaAttributesLoaded) {
			currentMana = ((ManaUsingEntity) livingEntity).manaattributes$getMana();
		}
		return currentMana;
	}

	public static float getCurrentStamina(LivingEntity livingEntity) {
		float currentStamina = 0.0F;
		if (isStaminaAttributesLoaded) {
			currentStamina = ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
		}
		return currentStamina;
	}

	public static void addMana(LivingEntity livingEntity, float amount) {
		if (isManaAttributesLoaded) {
			((ManaUsingEntity) livingEntity).manaattributes$addMana(amount);
		}
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		if (isStaminaAttributesLoaded) {
			((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
		}
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Spell Engine was extended!");

		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		PayloadTypeRegistry.playS2C().register(ServerConfigSyncPacket.PACKET_ID, ServerConfigSyncPacket.PACKET_CODEC);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, new ServerConfigSyncPacket(serverConfig));
		});
	}

	public record ServerConfigSyncPacket(ServerConfig serverConfig) implements CustomPayload {
		public static final CustomPayload.Id<ServerConfigSyncPacket> PACKET_ID = new CustomPayload.Id<>(identifier("server_config_sync"));
		public static final PacketCodec<RegistryByteBuf, ServerConfigSyncPacket> PACKET_CODEC = PacketCodec.of(ServerConfigSyncPacket::write, ServerConfigSyncPacket::new);

		public ServerConfigSyncPacket(RegistryByteBuf registryByteBuf) {
			this(new Gson().fromJson(registryByteBuf.readString(), ServerConfig.class));
		}

		private void write(RegistryByteBuf registryByteBuf) {
			registryByteBuf.writeString(new Gson().toJson(serverConfig));
		}

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return PACKET_ID;
		}
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}