package net.team.astera.impl.mixin.autototem;

import net.team.astera.impl.event.impl.autototem.EventPostTick;
import net.team.astera.impl.event.impl.autototem.EventTick;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.team.astera.impl.util.traits.Util.EVENT_BUS;
import static net.team.astera.allreqs.modules.Module.fullNullCheck;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "tick", at = @At("HEAD"))
    void preTickHook(CallbackInfo ci) {
        if (!fullNullCheck()) EVENT_BUS.post(new EventTick());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    void postTickHook(CallbackInfo ci) {
        if (!fullNullCheck()) EVENT_BUS.post(new EventPostTick());
    }
}
