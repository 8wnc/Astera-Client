package net.team.astera.impl.event.impl.autoarmor;

import net.team.astera.impl.event.Stage;
import net.team.astera.impl.event.Event;

public class PlayerMotionUpdate extends Event {
    private final Stage stage;
    public PlayerMotionUpdate(Stage stage)
    {
        this.stage = stage;
        //super(p_Era);
    }
    public Stage getStage() {
        return stage;
    }
}
