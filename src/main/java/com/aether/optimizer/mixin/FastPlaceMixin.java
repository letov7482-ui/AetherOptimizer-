package com.aether.optimizer.mixin;

import com.aether.optimizer.engine.FastPlace;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecraftClient.class)
public class FastPlaceMixin {
    @ModifyVariable(method = "doItemUse", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int modifyPlaceDelay(int delay) {
        return FastPlace.getPlaceDelay();
    }
}
