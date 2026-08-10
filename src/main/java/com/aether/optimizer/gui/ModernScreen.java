package com.aether.optimizer.gui;

import com.aether.optimizer.engine.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.Random;

public class ModernScreen extends Screen {
    private boolean heatGuard = TempGuard.isEnabled();
    private boolean fastPlace = FastPlace.isEnabled();
    private boolean smartRAM = SmartRAM.isEnabled();
    private boolean netBoost = NetworkOptimizer.isEnabled();
    private String status = "Ready";
    private String bestRenderer = "";
    private int gain = 0;
    private String temp = "--°C";
    private final Random rand = new Random();
    private final float[] px = new float[40], py = new float[40], ps = new float[40];

    public ModernScreen() {
        super(Text.literal("AetherOptimizer"));
        for (int i = 0; i < 40; i++) {
            px[i] = rand.nextFloat() * 1000;
            py[i] = rand.nextFloat() * 1000;
            ps[i] = 0.1f + rand.nextFloat() * 0.3f;
        }
    }

    @Override
    protected void init() {
        int cx = this.width / 2, y = 55;

        addDrawableChild(ButtonWidget.builder(Text.literal("⚡ OPTIMIZE"), btn -> {
            status = "Testing..."; clearChildren(); init();
            bestRenderer = RenderSwitcher.findAndApply();
            gain = RenderSwitcher.getEstimatedGain();
            status = "Best: " + bestRenderer + " | +" + gain + " FPS";
            clearChildren(); init();
        }).dimensions(cx - 60, y, 120, 20).build());
        y += 30;

        addDrawableChild(ButtonWidget.builder(
            Text.literal((heatGuard ? "✅" : "❌") + " Heat Guard"), btn -> {
                heatGuard = !heatGuard; TempGuard.setEnabled(heatGuard);
                clearChildren(); init();
            }).dimensions(cx - 60, y, 120, 20).build());
        y += 22;
        addDrawableChild(ButtonWidget.builder(
            Text.literal((fastPlace ? "✅" : "❌") + " Fast Place"), btn -> {
                fastPlace = !fastPlace; FastPlace.setEnabled(fastPlace);
                clearChildren(); init();
            }).dimensions(cx - 60, y, 120, 20).build());
        y += 22;
        addDrawableChild(ButtonWidget.builder(
            Text.literal((smartRAM ? "✅" : "❌") + " Smart RAM"), btn -> {
                smartRAM = !smartRAM; SmartRAM.setEnabled(smartRAM);
                clearChildren(); init();
            }).dimensions(cx - 60, y, 120, 20).build());
        y += 22;
        addDrawableChild(ButtonWidget.builder(
            Text.literal((netBoost ? "✅" : "❌") + " Net Boost"), btn -> {
                netBoost = !netBoost; NetworkOptimizer.setEnabled(netBoost);
                if (netBoost) NetworkOptimizer.optimize();
                clearChildren(); init();
            }).dimensions(cx - 60, y, 120, 20).build());
        y += 28;
        addDrawableChild(ButtonWidget.builder(Text.literal("🧹 Clear RAM"), btn -> {
            int f = SmartRAM.cleanNow(); status = "Freed " + f + " MB";
        }).dimensions(cx - 50, y, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> close())
            .dimensions(cx - 30, height - 30, 60, 20).build());

        temp = TempGuard.getTemperature() + "°C";
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        renderBackground(ctx, mx, my, delta);
        for (int x = 0; x < width; x += 32) for (int y = 0; y < height; y += 32) ctx.fill(x, y, x + 1, y + 1, 0xFF1A1A2E);
        PulseEffects.tick();
        for (int i = 0; i < 40; i++) { py[i] -= ps[i]; if (py[i] < 0) { py[i] = height; px[i] = rand.nextFloat() * width; } ctx.fill((int) px[i], (int) py[i], (int) px[i] + 2, (int) py[i] + 2, 0xFF00FF88); }
        PulseEffects.renderGlitchText(ctx, textRenderer, "AETHER OPTIMIZER", width / 2, 18, 0xFF00FF88);
        ctx.drawCenteredTextWithShadow(textRenderer, status, width / 2, 38, 0xFFAAAAAA);
        int bw = 160, bh = 4, bx = width / 2 - bw / 2, by = height - 45;
        ctx.fill(bx, by, bx + bw, by + bh, 0xFF1A1A2E);
        int fill = gain > 0 ? bw * gain / 40 : 0;
        ctx.fill(bx, by, bx + fill, by + bh, 0xFF00FF88);
        String info = bestRenderer.isEmpty() ? "Temp: " + temp : "Best: " + bestRenderer + " | +" + gain + " FPS | " + temp;
        ctx.drawCenteredTextWithShadow(textRenderer, info, width / 2, height - 55, 0xFF888888);
        super.render(ctx, mx, my, delta);
    }
              }
