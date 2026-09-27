package me.javiline.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class HotbarButtonsClient implements ClientModInitializer {

	private static KeyMapping hotbarLeftKey;
	private static KeyMapping hotbarRightKey;

	private static final KeyMapping.Category HOTBAR_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("hotbarbuttons", "hotbar_controls"));

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		hotbarLeftKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.hotbarbuttons.hotbar_left",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_LEFT_BRACKET,
				HOTBAR_CATEGORY
		));

		hotbarRightKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.hotbarbuttons.hotbar_right",
				GLFW.GLFW_KEY_RIGHT_BRACKET,
				HOTBAR_CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			while (hotbarLeftKey.consumeClick()) {
				shiftHotbarSlot(client, -1);
			}

			while (hotbarRightKey.consumeClick()) {
				shiftHotbarSlot(client, 1);
			}
		});
	}

	private void shiftHotbarSlot(Minecraft client, int direction) {
		if (client.player == null) return;

		int currentSlot = client.player.getInventory().getSelectedSlot();

		int newSlot = (currentSlot + direction) % 9;
		if (newSlot < 0) {
			newSlot += 9;
		}

		client.player.getInventory().setSelectedSlot(newSlot);
	}
}