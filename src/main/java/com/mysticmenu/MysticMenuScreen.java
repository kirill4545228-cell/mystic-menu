package com.mysticmenu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFW;

public class MysticMenuClient implements ClientModInitializer {

public static KeyBinding OPEN_MENU_KEY;

@Override
public void onInitializeClient() {

    OPEN_MENU_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                    "key.mysticmenu.open",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_RIGHT_SHIFT,
                    "category.mysticmenu.main"
            )
    );

    ClientTickEvents.END_CLIENT_TICK.register(client -> {

        while (OPEN_MENU_KEY.wasPressed()) {

            if (client.player == null) {
                continue;
            }

            client.setScreen(new MysticMenuScreen());
        }
    });
}

}
