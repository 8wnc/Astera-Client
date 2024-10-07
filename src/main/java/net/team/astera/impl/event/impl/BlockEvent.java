package net.team.astera.impl.event.impl;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos;
import net.team.astera.impl.event.Event;

public class BlockEvent extends Event {
    private final BlockPos pos;
    private final Direction facing; // EnumFacing yerine Direction
    private final int stage;

    public BlockEvent(BlockPos pos, Direction facing, int stage) { // EnumFacing yerine Direction
        this.pos = pos;
        this.facing = facing;
        this.stage = stage;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getFacing() { // EnumFacing yerine Direction
        return facing;
    }

    public int getStage() {
        return stage;
    }
}
