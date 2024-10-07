package net.team.astera.allreqs.modules.visual;

import org.jetbrains.annotations.NotNull;
import net.team.astera.allreqs.modules.Module;
import net.team.astera.allreqs.settings.Setting;

public class AspectRatio extends Module {
        private static AspectRatio INSTANCE = new AspectRatio();
    public AspectRatio() {
        super("AspectRatio", "AspectRatio", Module.Category.VISUAL, true, false, false);
        this.setInstance();
    }

    public static AspectRatio getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AspectRatio();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public Setting<Float> ratio = this.register(new Setting<>("Ratio", 1.78f, 0.1f, 5f));
}
