package net.team.astera.allreqs.modules.player;

import net.minecraft.client.util.math.MatrixStack;
import net.team.astera.impl.event.impl.extra.Render3DEvent;
import net.team.astera.allreqs.modules.Module;
import net.team.astera.allreqs.settings.Setting;

public class ViewLock extends Module {
    public ViewLock() {
        super("ViewLock", "YawLock", Category.PLAYER, true, false, false);
    }

    public Setting<Boolean> pitch = this.register(new Setting<>("Pitch", true));
    public Setting<Float> pitchValue = this.register(new Setting<>("PitchValue", 0f, (-90f), 90f, v -> pitch.getValue()));

    public Setting<Boolean> yaw = this.register(new Setting<>("Yaw", true));
    public Setting<Float> yawValue = this.register(new Setting<>("YawValue", 0f, (-180f), 180f, v -> pitch.getValue()));

    public void onRender3D(Render3DEvent m) { //Replace other
        if (pitch.getValue()) mc.player.setPitch(pitchValue.getValue());
        if (yaw.getValue()) mc.player.setYaw(yawValue.getValue());
    }
}
