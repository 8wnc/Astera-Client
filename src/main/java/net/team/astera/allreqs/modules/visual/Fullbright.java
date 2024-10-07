package net.team.astera.allreqs.modules.visual;

import net.team.astera.allreqs.modules.Module;
import net.team.astera.allreqs.settings.Setting;

public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright", "", Category.VISUAL, true, false, false);
    }

    private final Setting<Float> minBright = this.register(new Setting<>("MinBright", 0.5f, 0f, 1f));
}
