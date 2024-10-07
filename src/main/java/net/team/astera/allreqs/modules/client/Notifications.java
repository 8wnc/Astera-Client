package net.team.astera.allreqs.modules.client;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.team.astera.AsteraClient;
import net.team.astera.allreqs.commands.Command;
import net.team.astera.allreqs.modules.Module;
import net.team.astera.allreqs.settings.Setting;
import net.team.astera.impl.event.impl.ClientEvent;

import java.util.*;

public class Notifications extends Module {
    private static Notifications INSTANCE = new Notifications();
    private final Timer timer = new Timer();
    public Setting<Boolean> totemPops = this.register(new Setting<>("TotemPops", false));
    public Setting<Integer> delay = this.register(new Setting<>("Delay", 2000, 0, 5000, v -> this.totemPops.getValue(), "Delays messages."));
    public Setting<Boolean> clearOnLogout = this.register(new Setting<>("LogoutClear", false));
    public Setting<Boolean> moduleMessage = this.register(new Setting<>("ModuleMessage", false));
    public Setting<Boolean> list = this.register(new Setting<>("List", false, v -> this.moduleMessage.getValue()));
    public Setting<Boolean> watermark = this.register(new Setting<>("Watermark", false, v -> this.moduleMessage.getValue()));
    public Setting<Boolean> visualRange = this.register(new Setting<>("VisualRange", false));
    private Set<String> playersInRange = new HashSet<>();
    private Map<String, Integer> messageMap = new HashMap<>();
    private List<PlayerEntity> knownPlayers = new ArrayList<>();

    public Notifications() {
        super("Notifications", "Sends Messages.", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static Notifications getInstance() {
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<>();
    }

    @Override
    public void onUpdate() {
        if (mc.world == null || mc.player == null) {
            return;
        }

        if (this.visualRange.getValue()) {
            Set<String> playersInRangeThisUpdate = new HashSet<>();
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof PlayerEntity && entity != mc.player) {
                    double distance = mc.player.squaredDistanceTo(entity);
                    if (distance <= 10000) { // 100 blok mesafe
                        PlayerEntity player = (PlayerEntity) entity;
                        String playerName = player.getName().getString();
                        String playerCoords = String.format(" at (%.1f, %.1f, %.1f)", player.getX(), player.getY(), player.getZ());
                        playersInRangeThisUpdate.add(playerName);
                        if (!playersInRange.contains(playerName)) {
                            Formatting color = AsteraClient.friendManager.isFriend(player) ? Formatting.AQUA : Formatting.GRAY;
                            String message = playerName + Formatting.GRAY + " has entered your visual range" + playerCoords + ".";
                            Command.sendMessage(formatMessage(message, color));
                            mc.player.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 1.0f, 1.0f);
                        }
                    }
                }
            }

            for (String playerInSet : playersInRange) {
                if (!playersInRangeThisUpdate.contains(playerInSet)) {
                    Formatting color = AsteraClient.friendManager.isFriend(playerInSet) ? Formatting.AQUA : Formatting.GRAY;
                    String message = playerInSet + Formatting.GRAY + " has left your visual range.";
                    Command.sendMessage(formatMessage(message, color));
                    mc.player.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 1.0f, 1.0f);
                }
            }

            playersInRange = playersInRangeThisUpdate;
        }
    }

    private String formatMessage(String message, Formatting color) {
        return color + message + Formatting.RESET;
    }

    @Subscribe
    public void onToggleModule(ClientEvent event) {
        if (!this.moduleMessage.getValue()) {
            return;
        }

        Module module = (Module) event.getFeature();

        if (event.getStage() == 0 && this.list.getValue()) {
            Text text = this.watermark.getValue() ?
                    Text.literal(AsteraClient.commandManager.getClientMessage() + " ")
                            .styled(style -> style.withColor(Formatting.GRAY))
                            .append(Text.literal(module.getDisplayName() + " off.")
                                    .styled(style -> style.withColor(Formatting.RED))) :
                    Text.literal(module.getDisplayName() + " off.")
                            .styled(style -> style.withColor(Formatting.RED));
            Notifications.mc.inGameHud.getChatHud().addMessage(text);
        }

        if (event.getStage() == 1 && this.list.getValue()) {
            Text text = this.watermark.getValue() ?
                    Text.literal(AsteraClient.commandManager.getClientMessage() + " ")
                            .styled(style -> style.withColor(Formatting.GRAY))
                            .append(Text.literal(module.getDisplayName() + Formatting.GREEN + " on!")
                                    .styled(style -> style.withColor(Formatting.GREEN))) :
                    Text.literal(Formatting.GRAY + module.getDisplayName() + Formatting.GREEN + " on!")
                            .styled(style -> style.withColor(Formatting.GREEN));
            Notifications.mc.inGameHud.getChatHud().addMessage(text);
        }
    }
}
