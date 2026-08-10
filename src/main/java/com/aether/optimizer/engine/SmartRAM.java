package com.aether.optimizer.engine;

import com.aether.optimizer.AetherOptimizerMod;

public class SmartRAM {
    private static boolean enabled = false;
    private static long lastClean = 0;
    private static final int INTERVAL = 30000;

    public static void setEnabled(boolean e) { enabled = e; }
    public static boolean isEnabled() { return enabled; }

    public static int cleanNow() {
        long before = Runtime.getRuntime().freeMemory();
        System.gc();
        long after = Runtime.getRuntime().freeMemory();
        lastClean = System.currentTimeMillis();
        int freed = (int)((after - before) / (1024 * 1024));
        AetherOptimizerMod.LOGGER.info("RAM cleaned: {} MB", Math.max(freed, 0));
        return Math.max(freed, 0);
    }

    public static void startAutoClean() {
        if (!enabled || System.currentTimeMillis() - lastClean < INTERVAL) return;
        cleanNow();
    }
}
