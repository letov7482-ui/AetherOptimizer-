package com.aether.optimizer.engine;

import com.aether.optimizer.AetherOptimizerMod;
import net.minecraft.client.MinecraftClient;

public class FastPlace {
    private static boolean enabled = false;

    public static void setEnabled(boolean e) {
        enabled = e;
        AetherOptimizerMod.LOGGER.info("FastPlace: {}", enabled ? "ON" : "OFF");
    }

    public static boolean isEnabled() { return enabled; }

    public static void tick() {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;
        // Уменьшаем задержку между использованием предметов
        if (client.itemUseCooldown > 0) {
            client.itemUseCooldown = Math.max(0, client.itemUseCooldown - 1);
        }
    }
}
