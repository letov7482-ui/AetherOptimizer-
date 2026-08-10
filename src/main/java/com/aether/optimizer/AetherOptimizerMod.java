package com.aether.optimizer;

import com.aether.optimizer.engine.*;
import com.aether.optimizer.gui.ModernScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AetherOptimizerMod implements ModInitializer {
    public static final String MOD_ID = "aetheroptimizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("AetherOptimizer starting...");

        KeyBinding key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.aetheroptimizer.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "AetherOptimizer"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (key.wasPressed()) client.setScreen(new ModernScreen());
            TempGuard.startMonitoring();
            SmartRAM.startAutoClean();
            FastPlace.tick();
        });

        ScreenEvents.AFTER_INIT.register((client, screen, w, h) -> {
            if (screen instanceof TitleScreen) {
                Screens.getButtons(screen).add(
                    ButtonWidget.builder(Text.literal("⚡ Optimizer"), btn ->
                        client.setScreen(new ModernScreen())
                    ).dimensions(10, 10, 100, 20).build()
                );
            }
        });
    }
}
