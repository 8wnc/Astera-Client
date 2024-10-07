package net.team.astera.allreqs.modules.movement;

import net.team.astera.allreqs.modules.Module;


public class AutoWalk
        extends Module {
			public static AutoWalk INSTANCE = new AutoWalk();
        public AutoWalk() {
            super("AutoWalk", "automatic walk module", Category.MOVEMENT, true, false, false);
		        INSTANCE = this;
	}

	    public static AutoWalk getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoWalk();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

	@Override
	public void onDisable() {
		mc.options.forwardKey.setPressed(false);
	}

	@Override
	public void onUpdate() {
		mc.options.forwardKey.setPressed(true);
    }

}
