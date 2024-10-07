package net.team.astera.allreqs.modules.misc;

import net.team.astera.allreqs.modules.Module;
import net.minecraft.text.Text;

public class YawGetter extends Module {
    public YawGetter() {
        super("YawGetter", "For waypoint coord leak method", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        fullNullCheck();
        mc.player.sendMessage(Text.of(mc.player.getYaw() + ""));
        disable();
        super.onEnable();
    }
}