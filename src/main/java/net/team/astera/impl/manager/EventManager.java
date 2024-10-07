package net.team.astera.impl.manager;

import com.google.common.eventbus.Subscribe;
import net.team.astera.AsteraClient;
import net.team.astera.impl.event.Stage;
import net.team.astera.impl.event.impl.*;
import net.team.astera.allreqs.Feature;
import net.team.astera.allreqs.commands.Command;
import net.team.astera.impl.util.models.Timer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.Formatting;

public class EventManager extends Feature {
    private final Timer logoutTimer = new Timer();

    public void init() {
        EVENT_BUS.register(this);
    }

    public void onUnload() {
        EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        mc.getWindow().setTitle("AsteraClient b1.0.0-92au7db+mojava");
        if (!fullNullCheck()) {
//            AsteraClient.inventoryManager.update();
            AsteraClient.moduleManager.onUpdate();
            AsteraClient.moduleManager.sortModules(true);
            onTick();
//            if ((HUD.getInstance()).renderingMode.getValue() == HUD.RenderingMode.Length) {
//                AsteraClient.moduleManager.sortModules(true);
//            } else {
//                AsteraClient.moduleManager.sortModulesABC();
//            }
        }
    }

    public void onTick() {
        if (fullNullCheck())
            return;
        AsteraClient.moduleManager.onTick();
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            EVENT_BUS.post(new DeathEvent(player));
//            PopCounter.getInstance().onDeath(player);
        }
    }

    @Subscribe
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (fullNullCheck())
            return;
        if (event.getStage() == Stage.PRE) {
            AsteraClient.speedManager.updateValues();
            AsteraClient.rotationManager.updateRotations();
            AsteraClient.positionManager.updatePosition();
        }
        if (event.getStage() == Stage.POST) {
            AsteraClient.rotationManager.restoreRotations();
            AsteraClient.positionManager.restorePosition();
        }
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        AsteraClient.serverManager.onPacketReceived();
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket)
            AsteraClient.serverManager.update();
    }

    @Subscribe
    public void onWorldRender(Render3DEvent event) {
        AsteraClient.moduleManager.onRender3D(event);
    }

    @Subscribe public void onRenderGameOverlayEvent(Render2DEvent event) {
        AsteraClient.moduleManager.onRender2D(event);
    }

    @Subscribe public void onKeyInput(KeyEvent event) {
        AsteraClient.moduleManager.onKeyPressed(event.getKey());
    }

    @Subscribe public void onChatSent(ChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.cancel();
            try {
                if (event.getMessage().length() > 1) {
                    AsteraClient.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage(Formatting.RED + "An error occurred while running this command. Check the log!");
            }
        }
    }
}