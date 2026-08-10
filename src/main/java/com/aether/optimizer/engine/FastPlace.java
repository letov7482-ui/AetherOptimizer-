package com.aether.optimizer.engine;

public class FastPlace {
    private static boolean enabled = false;
    private static int delay = 0; // 0 = мгновенно

    public static void setEnabled(boolean e) { enabled = e; }
    public static boolean isEnabled() { return enabled; }

    public static int getPlaceDelay() { return enabled ? delay : 4; } // 4 = стандарт
}
