package net.team.astera.allreqs.modules.player;

import net.team.astera.allreqs.modules.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module{
	
	public AutoRespawn() {
        super("AutoRespawn", "AutoRespawn", Category.PLAYER, true, false, false);
	}

	@Override
	public void onUpdate() {
		if (mc.currentScreen instanceof DeathScreen) {
			mc.player.requestRespawn();
			mc.setScreen(null);
		}
    }
}