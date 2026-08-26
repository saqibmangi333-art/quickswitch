package com.quickswitch;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

public class QuickSwitchClient implements ClientModInitializer {
    private static KeyBinding autoTotemKey;

    @Override
    public void onInitializeClient() {
        autoTotemKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.quickswitch.totem",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.quickswitch"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.interactionManager == null) return;

            while (autoTotemKey.wasPressed()) {
                var player = client.player;
                if (player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) continue;

                for (int i = 0; i < player.getInventory().main.size(); i++) {
                    if (player.getInventory().main.get(i).isOf(Items.TOTEM_OF_UNDYING)) {
                        int slot = i < 9 ? i + 36 : i;
                        client.interactionManager.clickSlot(player.playerScreenHandler.syncId, slot, 40, SlotActionType.SWAP, player);
                        break;
                    }
                }
            }
        });
    }
}
