package net.team.astera.impl.manager;

import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import org.jetbrains.annotations.NotNull;

import com.google.common.eventbus.Subscribe;

import net.team.astera.impl.event.impl.cracked.PacketEvent;
import net.team.astera.allreqs.modules.combat.tntaura.util.Timer;

public class PlayerManager  {
    public int ticksElytraFlying, serverSideSlot;
    public boolean inInventory;
    public final Timer switchTimer = new Timer();

    //@EventHandler
    @Subscribe
    public void onSyncWithServer(PacketEvent.@NotNull Send event) {
        if (event.getPacket() instanceof ClickSlotC2SPacket) {
            inInventory = true;
        }
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket slot) {
            /*if (serverSideSlot == slot.getSelectedSlot() && !(ModuleManager.noSlow.isEnabled() && NoSlow.mode.getValue() == NoSlow.Mode.StrictNCP)) {
                event.cancel();
                ModuleManager.mainSettings.debug("Double slot packet!");
            }*/

            switchTimer.reset();
            serverSideSlot = slot.getSelectedSlot();
        }
        if (event.getPacket() instanceof CloseHandledScreenC2SPacket) {
            inInventory = false;
        }
    }

   // @EventHandler
   @Subscribe
    public void onPacketReceive(PacketEvent.@NotNull Receive event) {
        if (event.getPacket() instanceof UpdateSelectedSlotS2CPacket slot) {
            switchTimer.reset();
            serverSideSlot = slot.getSlot();
        }
    }

}
