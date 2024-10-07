package net.team.astera.allreqs.modules.movement;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.team.astera.allreqs.modules.Module;
import net.team.astera.allreqs.settings.Setting;
import net.team.astera.impl.event.impl.UpdateWalkingPlayerEvent;

public class PearlPhase extends Module {

    private final Setting<Float> clipDistance = this.register(new Setting<>("ClipDistance", 1.0f, 0.1f, 5.0f));
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private boolean pearlThrown = false;

    public PearlPhase() {
        super("PearlPhase", "Easily clip inside walls with pearls.", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (hasEnderPearl()) {
            throwPearl();
        } else {
            disable();
        }
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.world != null) {
            if (pearlThrown && shouldClip()) {
                Vec3d direction = mc.player.getRotationVec(1.0f);
                mc.player.updatePosition(
                        mc.player.getX() + direction.x * clipDistance.getValue(),
                        mc.player.getY() + direction.y * clipDistance.getValue(),
                        mc.player.getZ() + direction.z * clipDistance.getValue()
                );
                
                disable();
            }
        }
    }

    private boolean hasEnderPearl() {
        return mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL
                || mc.player.getOffHandStack().getItem() == Items.ENDER_PEARL;
    }

    private void throwPearl() {
        if (mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL) {
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            pearlThrown = true;
        } else if (mc.player.getOffHandStack().getItem() == Items.ENDER_PEARL) {
            mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
            pearlThrown = true;
        }
    }

    private boolean shouldClip() {
        return mc.crosshairTarget != null
                && mc.crosshairTarget.getType() == HitResult.Type.BLOCK;
    }
}
