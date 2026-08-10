package com.aether.optimizer.engine;

import com.aether.optimizer.AetherOptimizerMod;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.*;
import java.io.*;
import java.nio.file.*;

public class FpsBooster {
    private static boolean enabled = false;
    private static boolean jvmSaved = false;

    public static void setEnabled(boolean e) { enabled = e; }
    public static boolean isEnabled() { return enabled; }

    public static void applyAll() {
        if (!enabled) return;
        applyOpenGLUltra();
        applyMinecraftUltra();
        applySodiumUltra();
        applyIrisUltra();
        applyLauncherUltra();
        applyJVMUltra();
        applyThreadUltra();
        applyMemoryUltra();
        applyNetworkUltra();
        AetherOptimizerMod.LOGGER.info("ULTRA BOOST APPLIED");
    }

    private static void applyOpenGLUltra() {
        try {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_DITHER);
            GL11.glDisable(GL11.GL_STENCIL_TEST);
            GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_FASTEST);
            GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, GL11.GL_FASTEST);
            GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_FASTEST);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        } catch (Exception ignored) {}
    }

    private static void applyMinecraftUltra() {
        File f = new File(System.getProperty("user.dir"), "options.txt");
        if (!f.exists()) return;
        try {
            String c = Files.readString(f.toPath());
            c = c.replaceAll("renderDistance:\\d+", "renderDistance:8");
            c = c.replaceAll("simulationDistance:\\d+", "simulationDistance:6");
            c = c.replaceAll("graphicsMode:\\w+", "graphicsMode:fancy");
            c = c.replaceAll("ao:\\w+", "ao:true");
            c = c.replaceAll("enableVsync:\\w+", "enableVsync:false");
            c = c.replaceAll("enableClouds:\\w+", "enableClouds:fast");
            c = c.replaceAll("maxFps:\\d+", "maxFps:260");
            c = c.replaceAll("particles:\\w+", "particles:decreased");
            c = c.replaceAll("mipmapLevels:\\d+", "mipmapLevels:1");
            c = c.replaceAll("useVbo:\\w+", "useVbo:true");
            c = c.replaceAll("entityShadows:\\w+", "entityShadows:false");
            c = c.replaceAll("biomeBlendRadius:\\d+", "biomeBlendRadius:2");
            c = c.replaceAll("soundVolume:\\d+\\.\\d+", "soundVolume:0.0");
            c = c.replaceAll("ambientVolume:\\d+\\.\\d+", "ambientVolume:0.0");
            Files.writeString(f.toPath(), c);
            AetherOptimizerMod.LOGGER.info("Minecraft: ULTRA");
        } catch (Exception ignored) {}
    }

    private static void applySodiumUltra() {
        Path p = Paths.get(System.getProperty("user.dir"), "config/sodium-options.json");
        if (!p.toFile().exists()) return;
        try {
            String c = Files.readString(p);
            c = c.replaceAll("\"quality\"\\s*:\\s*\\{[^}]*\\}",
                "\"quality\": {\"weather_quality\":\"FAST\",\"leaves_quality\":\"FANCY\",\"enable_vignette\":true}");
            c = c.replaceAll("\"performance\"\\s*:\\s*\\{[^}]*\\}",
                "\"performance\": {\"chunk_builder_threads\":0,\"always_defer_chunk_updates\":true,\"animate_only_visible_textures\":true,\"use_entity_culling\":true,\"use_particle_culling\":true,\"use_fog_occlusion\":true,\"use_block_face_culling\":true,\"use_compact_vertex_format\":true,\"use_translucent_face_sorting\":false}");
            c = c.replaceAll("\"advanced\"\\s*:\\s*\\{[^}]*\\}",
                "\"advanced\": {\"enable_memory_tracing\":false,\"use_advanced_staging_buffers\":true,\"cpu_render_ahead_limit\":3,\"allow_direct_memory_access\":true,\"use_persistent_mapping\":true}");
            Files.writeString(p, c);
            AetherOptimizerMod.LOGGER.info("Sodium: ULTRA");
        } catch (Exception ignored) {}
    }

    private static void applyIrisUltra() {
        Path p = Paths.get(System.getProperty("user.dir"), "config/iris.properties");
        if (!p.toFile().exists()) return;
        try {
            String c = Files.readString(p);
            c = c.replaceAll("maxShadowRenderDistance=\\d+", "maxShadowRenderDistance=4");
            if (!c.contains("maxShadowRenderDistance")) c += "\nmaxShadowRenderDistance=4";
            Files.writeString(p, c);
            AetherOptimizerMod.LOGGER.info("Iris: ULTRA");
        } catch (Exception ignored) {}
    }

    private static void applyLauncherUltra() {
        String[] paths = {
            "/storage/emulated/0/Android/data/net.kdt.pojavlaunch/files/config.json",
            "/storage/emulated/0/Android/data/io.github.fold.launcher/files/config.json",
            "/storage/emulated/0/Android/data/com.movtery.zalithlauncher/files/config.json"
        };
        for (String path : paths) {
            File f = new File(path);
            if (f.exists()) {
                try {
                    String c = Files.readString(f.toPath());
                    c = c.replaceAll("\"resolution\"\\s*:\\s*\\d+", "\"resolution\": 85");
                    c = c.replaceAll("\"forceVsync\"\\s*:\\s*\\w+", "\"forceVsync\": false");
                    Files.writeString(f.toPath(), c);
                    return;
                } catch (Exception ignored) {}
            }
        }
    }

    private static void applyJVMUltra() {
        if (jvmSaved) return;
        jvmSaved = true;
        int ram = (int)(Runtime.getRuntime().maxMemory() / (1024*1024));
        int halfRam = Math.min(ram / 2, 2048);
        String args = "-XX:+UseZGC -XX:+DisableExplicitGC -Djava.awt.headless=true " +
            "-Xms512M -Xmx" + halfRam + "M -XX:+AlwaysPreTouch " +
            "-XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=30 " +
            "-XX:+UseStringDeduplication -XX:+OptimizeStringConcat " +
            "-XX:+UseLargePages -XX:+UseNUMA";
        File f = new File(System.getProperty("user.dir"), "aether_ultra_jvm_args.txt");
        try { Files.writeString(f.toPath(), args);
            AetherOptimizerMod.LOGGER.info("JVM: ULTRA");
        } catch (Exception ignored) {}
    }

    private static void applyThreadUltra() {
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            new ProcessBuilder("renice", "-20", String.valueOf(ProcessHandle.current().pid()))
                .redirectErrorStream(true).start();
        } catch (Exception ignored) {}
    }

    private static void applyMemoryUltra() {
        long max = Runtime.getRuntime().maxMemory();
        long free = Runtime.getRuntime().freeMemory();
        if (free < max * 0.3) {
            System.gc();
        }
    }

    private static void applyNetworkUltra() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("http.keepAlive", "false");
        System.setProperty("sun.net.client.defaultConnectTimeout", "2000");
        System.setProperty("sun.net.client.defaultReadTimeout", "2000");
        System.setProperty("sun.net.inetaddr.ttl", "15");
        System.setProperty("networkaddress.cache.ttl", "15");
    }
                }
