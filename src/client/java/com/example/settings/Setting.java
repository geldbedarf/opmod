package com.example.settings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class Setting {
    protected final String name;
    protected final String description;

    public Setting(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void render(DrawContext ctx, TextRenderer tr, int x, int y, int mouseX, int mouseY);

    public abstract boolean mouseClicked(double mouseX, double mouseY);

    public abstract Object getValue();

    public abstract void setValue(Object value);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}