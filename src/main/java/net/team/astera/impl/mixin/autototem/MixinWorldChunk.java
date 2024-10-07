package net.team.astera.impl.mixin.autototem;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.team.astera.impl.event.impl.autototem.EventSetBlockState;
import static net.team.astera.impl.util.traits.Util.EVENT_BUS;

@Mixin({WorldChunk.class})
public class MixinWorldChunk {

    @Shadow @Final World world;

    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void setBlockStateHook(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        if (world.isClient) {
            EVENT_BUS.post(new EventSetBlockState(pos, cir.getReturnValue(), state));
        }
    }
}