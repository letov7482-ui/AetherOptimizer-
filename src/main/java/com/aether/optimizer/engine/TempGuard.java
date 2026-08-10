package com.aether.optimizer.engine;

import com.aether.optimizer.AetherOptimizerMod;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class TempGuard {
    private static boolean enabled = false;
    private static int lastTemp = 38;
    private static long lastCheck = 0;

    public static void setEnabled(boolean e) { enabled = e; }
    public static boolean isEnabled() { return enabled; }

    public static int getTemperature() {
        if (System.currentTimeMillis() - lastCheck < 5000) return lastTemp;
        lastCheck = System.currentTimeMillis();
        try {
            for (int i = 0; i < 10; i++) {
                File f = new File("/sys/class/thermal/thermal_zone" + i + "/temp");
                if (f.exists()) {
                    String s = new Scanner(f).useDelimiter("\\A").next().trim();
                    lastTemp = Integer.parseInt(s) / 1000;
                    return lastTemp;
                }
            }
        } catch (Exception e) { lastTemp = 38; }
        return lastTemp;
    }

    public static void startMonitoring() {
        if (!enabled) return;
        int t = getTemperature();
        if (t >= 47) applyPreset(2);
        else if (t >= 44) applyPreset(4);
        else if (t >= 41) applyPreset(6);
        else applyPreset(8);
    }

    private static void applyPreset(int rd) {
        File f = new File(System.getProperty("user.dir"), "options.txt");
        if (f.exists()) {
            try {
                String c = Files.readString(f.toPath());
                c = c.replaceAll("renderDistance:\\d+", "renderDistance:" + rd);
                Files.writeString(f.toPath(), c);
                AetherOptimizerMod.LOGGER.info("TempGuard: renderDistance={} ({}°C)", rd, lastTemp);
            } catch (IOException ignored) {}
        }
    }
}
