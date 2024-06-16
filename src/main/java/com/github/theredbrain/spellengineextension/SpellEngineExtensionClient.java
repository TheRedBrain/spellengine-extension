package com.github.theredbrain.spellengineextension;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SpellEngineExtensionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Packets
		ClientPlayNetworking.registerGlobalReceiver(SpellEngineExtension.ServerConfigSync.ID, (client, handler, buf, responseSender) -> {
			SpellEngineExtension.serverConfig = SpellEngineExtension.ServerConfigSync.read(buf);
		});
	}
}