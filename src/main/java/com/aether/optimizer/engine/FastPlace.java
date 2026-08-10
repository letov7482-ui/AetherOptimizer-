package com.aether.optimizer.engine;

import com.aether.optimizer.AetherOptimizerMod;
import net.minecraft.client.MinecraftClient;

public class FastPlace {
    private static boolean enabled = false;
    private static long lastClick = 0;
    private static final int FAST_DELAY = 50;  // 50ms = сверхбыстро
    private static final int NORMAL_DELAY = 200; // 200ms = стандарт

    public static void setEnabled(boolean e) {
        enabled = e;
        AetherOptimizerMod.LOGGER.info("FastPlace: {}", enabled ? "ON (50ms)" : "OFF (200ms)");
    }

    public static boolean isEnabled() { return enabled; }

    /**
     * Вызывается каждый тик. Если FastPlace включён — ускоряет клики.
     */
    public static void tick() {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        long now = System.currentTimeMillis();
        if (now - lastClick >= FAST_DELAY) {
            lastClick = now;
            // Симулируем быстрое нажатие — сбрасываем кулдаун использования предмета
            if (client.player.getItemUseTime() > 0) {
                client.player.stopUsingItem();
            }
        }
    }

    /**
     * Возвращает задержку между кликами в миллисекундах.
     */
    public static int getDelay() {
        return enabled ? FAST_DELAY : NORMAL_DELAY;
    }
}
