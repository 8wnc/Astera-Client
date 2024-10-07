package net.team.astera.impl.manager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.team.astera.AsteraClient;
import net.team.astera.allreqs.Feature;
import net.team.astera.allreqs.commands.Command;
import net.team.astera.allreqs.modules.client.Notifications;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TotemPopManager extends Feature {
    private Notifications notifications;
    private Map<PlayerEntity, Integer> poplist = new ConcurrentHashMap<>();
    private final Set<PlayerEntity> toAnnounce = new HashSet<>();

    public void onUpdate() {
        if (this.notifications.isOn() && this.notifications.totemPops.getValue().booleanValue()) {
            for (PlayerEntity player : this.toAnnounce) {
                if (player == null) continue;

                // Mesaj formatlaması için playerName kullanılacak
                String playerName = player.getName().getString();
                int totemPops = this.getTotemPops(player);

                String message = Formatting.GRAY + playerName + " popped " + Formatting.GREEN + totemPops + Formatting.GRAY + " totem" + (totemPops == 1 ? "" : "s") + ".";
                Command.sendMessage(message);

                this.toAnnounce.remove(player);
                break;
            }
        }
    }

    public void onLogout() {
        this.onOwnLogout(this.notifications.clearOnLogout.getValue());
    }

    public void init() {
        this.notifications = AsteraClient.moduleManager.getModuleByClass(Notifications.class);
    }

    public void onTotemPop(PlayerEntity player) {
        this.popTotem(player);
        if (!player.equals(TotemPopManager.mc.player)) {
            this.toAnnounce.add(player);
        }
    }

    public void onDeath(PlayerEntity player) {
        if (this.getTotemPops(player) != 0 && !player.equals(TotemPopManager.mc.player) && this.notifications.isOn() && this.notifications.totemPops.getValue()) {
            String playerName = player.getName().getString();
            int totemPops = this.getTotemPops(player);

            String message = Formatting.GRAY + playerName + " died after popping " + Formatting.GREEN + totemPops + Formatting.GRAY + " totem" + (totemPops == 1 ? "" : "s") + ".";
            Command.sendMessage(message);

            this.toAnnounce.remove(player);
        }
        this.resetPops(player);
    }

    public void onLogout(PlayerEntity player, boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }

    public void onOwnLogout(boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }

    public void clearList() {
        this.poplist.clear();
    }

    public void resetPops(PlayerEntity player) {
        this.setTotemPops(player, 0);
    }

    public void popTotem(PlayerEntity player) {
        this.poplist.merge(player, 1, Integer::sum);
    }

    public void setTotemPops(PlayerEntity player, int amount) {
        this.poplist.put(player, amount);
    }

    public int getTotemPops(PlayerEntity player) {
        return this.poplist.getOrDefault(player, 0);
    }

    public String getTotemPopString(PlayerEntity player) {
        return Formatting.WHITE + (this.getTotemPops(player) <= 0 ? "" : "-" + this.getTotemPops(player) + " ");
    }
}
