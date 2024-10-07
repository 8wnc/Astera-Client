package net.team.astera.impl.event.impl;

import net.team.astera.impl.event.Event;
import net.team.astera.impl.event.Stage;

public class UpdateWalkingPlayerEvent extends Event {
    private final Stage stage;

    public UpdateWalkingPlayerEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
