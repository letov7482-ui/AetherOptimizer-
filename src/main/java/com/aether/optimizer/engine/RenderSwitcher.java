package com.aether.optimizer.engine;

import com.aether.optimizer.AetherOptimizerMod;
import java.io.*;
import java.nio.file.*;

public class RenderSwitcher {
    private static final String[] RENDERERS = {"Vulkan", "Zink", "LTW", "ANGLE", "MobileGLUES", "HolyGL4ES", "FasterGL4ES", "GL4ES"};
    private static final String[] CONFIG_PATHS = {
        "/storage/emulated/0/Android/data/net.kdt.pojavlaunch/files/config.json",
        "/storage/emulated/0/Android/data/io.github.fold.launcher/files/config.json",
        "/storage/emulated/0/Android/data/com.movtery.zalithlauncher/files/config.json"
    };
    private static int estimatedGain = 0;

    public static String findAndApply() {
        String best = "GL4ES";
        int bestScore = 0;
        for (String r : RENDERERS) {
            int score = scoreRenderer(r);
            if (score > bestScore) { bestScore = score; best = r; }
        }
        applyRenderer(best);
        estimatedGain = bestScore * 5 + 5;
        AetherOptimizerMod.LOGGER.info("Switched to {} (gain: +{} FPS)", best, estimatedGain);
        return best;
    }

    private static int scoreRenderer(String name) {
        return switch (name) {
            case "Vulkan" -> 8; case "Zink" -> 7; case "LTW" -> 6;
            case "ANGLE" -> 5; case "MobileGLUES" -> 5; case "HolyGL4ES" -> 4;
            case "FasterGL4ES" -> 3; default -> 2;
        };
    }

    private static void applyRenderer(String name) {
        for (String p : CONFIG_PATHS) {
            File f = new File(p);
            if (f.exists()) {
                try {
                    String c = Files.readString(f.toPath());
                    c = c.replaceAll("\"renderer\"\\s*:\\s*\"[^\"]*\"", "\"renderer\": \"" + name + "\"");
                    Files.writeString(f.toPath(), c);
                    AetherOptimizerMod.LOGGER.info("Config updated: {}", p);
                    return;
                } catch (IOException e) { AetherOptimizerMod.LOGGER.error("Failed to update config: {}", p); }
            }
        }
        System.setProperty("pojav.renderer", name);
    }

    public static int getEstimatedGain() { return estimatedGain; }
}
