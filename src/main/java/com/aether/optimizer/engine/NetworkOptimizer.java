package com.aether.optimizer.engine;

import com.aether.optimizer.AetherOptimizerMod;

public class NetworkOptimizer {
    private static boolean enabled = false;

    public static void setEnabled(boolean e) { enabled = e; }
    public static boolean isEnabled() { return enabled; }

    public static void optimize() {
        if (!enabled) return;
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("http.keepAlive", "false");
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
        AetherOptimizerMod.LOGGER.info("Network optimized");
    }
}
