package net.team.astera.impl.mixin.autototem;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.team.astera.impl.event.impl.autototem.EventEntitySpawn;
import static net.team.astera.impl.util.traits.Util.EVENT_BUS;
import java.awt.*;


@Mixin(ClientWorld.class)
public class MixinClientWorld {
    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    public void onAddEntity(Entity entity, CallbackInfo ci) {
        EventEntitySpawn ees = new EventEntitySpawn(entity);
        EVENT_BUS.post(ees);
        if (ees.isCancelled()) {
            ci.cancel();
        }
    }
}