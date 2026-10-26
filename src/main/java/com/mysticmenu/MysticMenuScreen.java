package com.mysticmenu;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MysticMenuScreen extends Screen {

private static final int PURPLE = 0xFFB58AFF;
private static final int PANEL = 0xF0191628;
private static final int CARD = 0xFF29223D;
private static final int BORDER = 0xFF7653A6;

private static final int PANEL_WIDTH = 320;
private static final int PANEL_HEIGHT = 240;

public MysticMenuScreen() {
    super(Text.literal("Mystic Menu"));
}

@Override
protected void init() {
    int x = (width - PANEL_WIDTH) / 2;
    int y = (height - PANEL_HEIGHT) / 2;

    int buttonX = x + 35;
    int buttonWidth = PANEL_WIDTH - 70;

    addDrawableChild(
            ButtonWidget.builder(
                    Text.literal("☀  Погода: ясно"),
                    button -> sendCommand("weather clear")
            ).dimensions(buttonX, y + 75, buttonWidth, 25).build()
    );

    addDrawableChild(
            ButtonWidget.builder(
                    Text.literal("◷  Установить день"),
                    button -> sendCommand("time set day")
            ).dimensions(buttonX, y + 107, buttonWidth, 25).build()
    );

    addDrawableChild(
            ButtonWidget.builder(
                    Text.literal("✦  Выдать алмаз"),
                    button -> sendCommand("give @s minecraft:diamond 1")
            ).dimensions(buttonX, y + 139, buttonWidth, 25).build()
    );

    addDrawableChild(
            ButtonWidget.builder(
                    Text.literal("Закрыть"),
                    button -> close()
            ).dimensions(buttonX, y + 180, buttonWidth, 22).build()
    );
}

private void sendCommand(String command) {
    if (client != null && client.getNetworkHandler() != null) {
        client.getNetworkHandler().sendChatCommand(command);
    }
}

@Override
public void render(
        DrawContext context,
        int mouseX,
        int mouseY,
        float delta
) {
    context.fill(0, 0, width, height, 0x99080015);

    int x = (width - PANEL_WIDTH) / 2;
    int y = (height - PANEL_HEIGHT) / 2;

    context.fill(
            x - 2, y - 2,
            x + PANEL_WIDTH + 2, y + PANEL_HEIGHT + 2,
            BORDER
    );

    context.fill(
            x, y,
            x + PANEL_WIDTH, y + PANEL_HEIGHT,
            PANEL
    );

    context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("✦ MYSTIC MENU ✦"),
            width / 2,
            y + 20,
            PURPLE
    );

    context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("Магия управления миром"),
            width / 2,
            y + 43,
            0xFFC8BEDD
    );

    context.fill(
            x + 20, y + 61,
            x + PANEL_WIDTH - 20, y + 63,
            CARD
    );

    super.render(context, mouseX, mouseY, delta);
}

@Override
public boolean shouldPause() {
    return false;
}

}
