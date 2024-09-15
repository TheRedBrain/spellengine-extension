package com.github.theredbrain.spellengineextension;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SpellEngineExtensionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SpellEngineExtension.ServerConfigSyncPacket.PACKET_ID, (payload, context) -> {
			SpellEngineExtension.serverConfig = payload.serverConfig();
		});
	}
}