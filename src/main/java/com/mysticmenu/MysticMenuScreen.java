package com.mysticmenu;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class MysticMenuScreen extends Screen {

private static final int PURPLE = 0xFFB58AFF;
private static final int LIGHT_PURPLE = 0xFFE3D3FF;
private static final int BACKGROUND = 0xEE171323;
private static final int CARD = 0xFF252036;
private static final int BORDER = 0xFF75539E;
private static final int MUTED = 0xFFB0A5C8;

private static final int PANEL_WIDTH = 360;
private static final int PANEL_HEIGHT = 350;

private static final Pattern ITEM_ID_PATTERN =
        Pattern.compile("^[a-z0-9_.-]+:[a-z0-9/._-]+$");

private TextFieldWidget itemField;
private TextFieldWidget amountField;

public MysticMenuScreen() {
    super(Text.literal("Mystic Menu"));
}

@Override
protected void init() {

    int x = (width - PANEL_WIDTH) / 2;
    int y = (height - PANEL_HEIGHT) / 2;

    int left = x + 20;
    int buttonWidth = (PANEL_WIDTH - 48) / 2;
    int right = left + buttonWidth + 8;

    // ПОГОДА

    addActionButton(
            left, y + 88, buttonWidth,
            "☀ Ясно",
            "weather clear"
    );

    addActionButton(
            right, y + 88, buttonWidth,
            "☂ Дождь",
            "weather rain"
    );

    addActionButton(
            left, y + 116, buttonWidth,
            "⚡ Гроза",
            "weather thunder"
    );

    // ВРЕМЯ СУТОК

    addActionButton(
            right, y + 116, buttonWidth,
            "Утро",
            "time set 0"
    );

    addActionButton(
            left, y + 144, buttonWidth,
            "День",
            "time set noon"
    );

    addActionButton(
            right, y + 144, buttonWidth,
            "Ночь",
            "time set midnight"
    );

    // ПОЛЕ ID ПРЕДМЕТА

    itemField = new TextFieldWidget(
            textRenderer,
            left,
            y + 207,
            PANEL_WIDTH - 40,
            20,
            Text.literal("ID предмета")
    );

    itemField.setMaxLength(128);
    itemField.setText("minecraft:diamond");

    addDrawableChild(itemField);

    // КОЛИЧЕСТВО

    amountField = new TextFieldWidget(
            textRenderer,
            left,
            y + 251,
            72,
            20,
            Text.literal("Количество")
    );

    amountField.setMaxLength(3);
    amountField.setText("1");

    addDrawableChild(amountField);

    // ВЫДАЧА ПРЕДМЕТА

    addDrawableChild(
            ButtonWidget.builder(
                    Text.literal("✦ Выдать"),
                    button -> giveItem()
            )
            .dimensions(
                    left + 82,
                    y + 251,
                    PANEL_WIDTH - 122,
                    20
            )
            .build()
    );

    // ЗАКРЫТИЕ МЕНЮ

    addDrawableChild(
            ButtonWidget.builder(
                    Text.literal("Закрыть"),
                    button -> close()
            )
            .dimensions(
                    left,
                    y + 294,
                    PANEL_WIDTH - 40,
                    22
            )
            .build()
    );
}

private void addActionButton(
        int x,
        int y,
        int buttonWidth,
        String label,
        String command
) {

    addDrawableChild(
            ButtonWidget.builder(
                    Text.literal(label),
                    button -> executeCommand(command)
            )
            .dimensions(x, y, buttonWidth, 22)
            .build()
    );
}

private void executeCommand(String command) {

    if (client == null || client.getNetworkHandler() == null) {
        return;
    }

    client.getNetworkHandler().sendChatCommand(command);
}

private void giveItem() {

    String itemId = itemField.getText().trim();

    if (!ITEM_ID_PATTERN.matcher(itemId).matches()) {
        itemField.setEditableColor(0xFFFF5555);
        return;
    }

    int amount;

    try {
        amount = Integer.parseInt(amountField.getText());
    } catch (NumberFormatException exception) {
        amountField.setEditableColor(0xFFFF5555);
        return;
    }

    if (amount < 1 || amount > 64) {
        amountField.setEditableColor(0xFFFF5555);
        return;
    }

    executeCommand("give @s " + itemId + " " + amount);
}

@Override
public void render(
        DrawContext context,
        int mouseX,
        int mouseY,
        float delta
) {

    int x = (width - PANEL_WIDTH) / 2;
    int y = (height - PANEL_HEIGHT) / 2;

    // Затемнение игрового мира

    context.fill(
            0, 0, width, height,
            0x99080512
    );

    // Внешняя фиолетовая рамка

    context.fill(
            x - 3, y - 3,
            x + PANEL_WIDTH + 3,
            y + PANEL_HEIGHT + 3,
            BORDER
    );

    // Основная панель

    context.fill(
            x, y,
            x + PANEL_WIDTH,
            y + PANEL_HEIGHT,
            BACKGROUND
    );

    // Верхняя декоративная линия

    context.fill(
            x + 14, y + 12,
            x + PANEL_WIDTH - 14,
            y + 14,
            PURPLE
    );

    // Заголовок

    context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("✦ MYSTIC MENU ✦"),
            width / 2,
            y + 24,
            LIGHT_PURPLE
    );

    context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("Магия управления миром"),
            width / 2,
            y + 43,
            MUTED
    );

    // Карточка погоды и времени

    drawCard(
            context,
            x + 12,
            y + 67,
            PANEL_WIDTH - 24,
            112,
            "ПОГОДА И ВРЕМЯ"
    );

    // Карточка предметов

    drawCard(
            context,
            x + 12,
            y + 187,
            PANEL_WIDTH - 24,
            94,
            "ВЫДАЧА ПРЕДМЕТОВ"
    );

    // Подписи

    context.drawTextWithShadow(
            textRenderer,
            Text.literal("ID предмета"),
            x + 20,
            y + 193,
            LIGHT_PURPLE
    );

    context.drawTextWithShadow(
            textRenderer,
            Text.literal("Кол-во"),
            x + 20,
            y + 237,
            LIGHT_PURPLE
    );

    // Нижняя подпись

    context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("MYSTIC MENU  •  v1.0.0"),
            width / 2,
            y + 328,
            MUTED
    );

    // Отрисовка кнопок и полей ввода

    super.render(context, mouseX, mouseY, delta);
}

private void drawCard(
        DrawContext context,
        int x,
        int y,
        int cardWidth,
        int cardHeight,
        String title
) {

    // Рамка карточки

    context.fill(
            x, y,
            x + cardWidth,
            y + cardHeight,
            BORDER
    );

    // Внутренняя часть

    context.fill(
            x + 1, y + 1,
            x + cardWidth - 1,
            y + cardHeight - 1,
            CARD
    );

    // Название раздела

    context.drawTextWithShadow(
            textRenderer,
            Text.literal(title),
            x + 8,
            y + 5,
            PURPLE
    );
}

@Override
public boolean shouldPause() {
    return false;
}

}
