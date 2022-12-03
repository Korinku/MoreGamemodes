package me.korinku.moga.content.client;

import me.korinku.moga.MoreGamemodes;
import me.korinku.moga.content.entities.EntityCreatePacket;
import me.korinku.moga.content.entities.SoulEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class MoreGamemodesClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		EntityRendererRegistry.register(MoreGamemodes.END_PROJECTILE, SoulEntityRenderer::new);

		ClientPlayNetworking.registerGlobalReceiver(EntityCreatePacket.ID, EntityCreatePacket::onPacket);

	}

}
