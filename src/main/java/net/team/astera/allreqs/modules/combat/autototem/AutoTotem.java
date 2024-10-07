package net.team.astera.allreqs.modules.combat.autototem;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.team.astera.allreqs.modules.combat.autocrystal.AutoCrystal;
import net.team.astera.allreqs.settings.Setting;
import org.jetbrains.annotations.NotNull;
import com.google.common.eventbus.Subscribe;

import net.team.astera.AsteraClient;
import net.team.astera.impl.event.impl.cracked.PacketEvent;
import net.team.astera.allreqs.modules.Module;
import net.team.astera.allreqs.modules.combat.autototem.utils.SearchInvResult;
import net.team.astera.allreqs.modules.combat.autototem.utils.ExplosionUtility;
import net.team.astera.allreqs.modules.combat.autototem.utils.PredictUtility;
import net.team.astera.allreqs.modules.combat.autototem.utils.InventoryUtility;
import net.team.astera.impl.event.impl.autototem.EventSync;

public final class AutoTotem extends Module {
    private static AutoTotem INSTANCE = new AutoTotem();
    private final Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.Matrix));
    private final Setting<OffHand> offhand = this.register(new Setting<>("Item", OffHand.Totem));
    private final Setting<Float> healthF = this.register(new Setting<>("HP", 16f, 0f, 36f));
    private final Setting<Float> healthS = this.register(new Setting<>("ShieldGappleHp", 16f, 0f, 20f, v -> offhand.getValue() == OffHand.Shield));
    private final Setting<Boolean> calcAbsorption = this.register(new Setting<>("CalcAbsorption", true));
    private final Setting<Boolean> stopMotion = this.register(new Setting<>("stopMotion", false));
    private final Setting<Boolean> resetAttackCooldown = this.register(new Setting<>("ResetAttackCooldown", false));
    private final Setting<Boolean> safety = new Setting<>("Safety", true);
    private final Setting<Boolean> hotbarFallBack = this.register(new Setting<>("HotbarFallback", false)).withParent(safety);
    private final Setting<Boolean> fallBackCalc = this.register(new Setting<>("FallBackCalc", true, v -> hotbarFallBack.getValue())).withParent(safety);
    private final Setting<Boolean> onElytra = this.register(new Setting<>("OnElytra", true)).withParent(safety);
    private final Setting<Boolean> onFall = this.register(new Setting<>("OnFall", true)).withParent(safety);
    private final Setting<Boolean> onCrystal = this.register(new Setting<>("OnCrystal", true)).withParent(safety);
    private final Setting<Boolean> onObsidianPlace = this.register(new Setting<>("OnObsidianPlace", false)).withParent(safety);
    private final Setting<Boolean> onCrystalInHand = this.register(new Setting<>("OnCrystalInHand", false)).withParent(safety);
    private final Setting<Boolean> onMinecartTnt = this.register(new Setting<>("OnMinecartTNT", true)).withParent(safety);
    private final Setting<Boolean> onCreeper = this.register(new Setting<>("OnCreeper", true)).withParent(safety);
    private final Setting<Boolean> onAnchor = this.register(new Setting<>("OnAnchor", true)).withParent(safety);
    public final Setting<Boolean> debug = this.register(new Setting<>("Debug", false)).withParent(safety);
    private final Setting<Boolean> onTnt = this.register(new Setting<>("OnTNT", true)).withParent(safety);
    private final Setting<Boolean> rcGap = this.register(new Setting<>("RCGap", false));
    private final Setting<Boolean> crappleSpoof = this.register(new Setting<>("CrappleSpoof", true, v -> offhand.getValue() == OffHand.GApple));

    private enum OffHand {Totem, Crystal, GApple, Shield}
    private enum Mode {Default, Matrix, MatrixPick, NewVersion}

    private int delay;
    private Item prevItem;

    public AutoTotem() {
        super("AutoTotem", "AutoTotem", Category.COMBAT, true, false, false);
        this.setInstance();
    }

    public static AutoTotem getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoTotem();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Subscribe
    public void onSync(EventSync e) {
        swapTo(getItemSlot());
        delay--;
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.@NotNull Receive e) {
        if (e.getPacket() instanceof EntitySpawnS2CPacket spawn && hotbarFallBack.getValue()) {
            if (spawn.getEntityType() == EntityType.END_CRYSTAL) {
                if (mc.player.squaredDistanceTo(spawn.getX(), spawn.getY(), spawn.getZ()) < 36) {
                    if (fallBackCalc.getValue() && ExplosionUtility.getSelfExplosionDamage(new Vec3d(spawn.getX(), spawn.getY(), spawn.getZ()), AutoCrystal.getInstance().selfPredictTicks.getValue()) < getTriggerHealth() + 4f)
                        return;
                    runInstant();
                }
            }
        }
        if (e.getPacket() instanceof BlockUpdateS2CPacket blockUpdate) {
            if (blockUpdate.getState().getBlock() == Blocks.OBSIDIAN && onObsidianPlace.getValue()) {
                if (mc.player.squaredDistanceTo(blockUpdate.getPos().toCenterPos()) < 36 && delay <= 0) {
                    runInstant();
                }
            }
        }
    }

    private float getTriggerHealth() {
        return mc.player.getHealth() + (calcAbsorption.getValue() ? mc.player.getAbsorptionAmount() : 0f);
    }

    private void runInstant() {
        SearchInvResult hotbarResult = InventoryUtility.findItemInHotBar(Items.TOTEM_OF_UNDYING);
        SearchInvResult invResult = InventoryUtility.findItemInInventory(Items.TOTEM_OF_UNDYING);
        if (hotbarResult.found()) {
            hotbarResult.switchTo();
            delay = 20;
        } else if (invResult.found()) {
            int slot = invResult.slot() >= 36 ? invResult.slot() - 36 : invResult.slot();
            if (!hotbarFallBack.getValue()) swapTo(slot);
            else mc.interactionManager.pickFromInventory(slot);
            delay = 20;
        }
    }

    public void swapTo(int slot) {
        if (slot != -1 && delay <= 0) {
            if (mc.currentScreen instanceof GenericContainerScreen) return;

            if (stopMotion.getValue()) mc.player.setVelocity(0, mc.player.getVelocity().getY(), 0);

            int nearest_slot = findNearestCurrentItem();
            int prevCurrentItem = mc.player.getInventory().selectedSlot;
            if (slot >= 9) {
                switch (mode.getValue()) {
                    case Default -> {
                        sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                        clickSlot(slot, nearest_slot, SlotActionType.SWAP);
                        clickSlot(45, nearest_slot, SlotActionType.SWAP);
                        sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                    }
                    case Matrix -> {
                        sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, nearest_slot, SlotActionType.SWAP, mc.player);
                        sendPacket(new UpdateSelectedSlotC2SPacket(nearest_slot));
                        mc.player.getInventory().selectedSlot = nearest_slot;
                        ItemStack itemstack = mc.player.getOffHandStack();
                        mc.player.setStackInHand(Hand.OFF_HAND, mc.player.getMainHandStack());
                        mc.player.setStackInHand(Hand.MAIN_HAND, itemstack);
                        sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
                        sendPacket(new UpdateSelectedSlotC2SPacket(prevCurrentItem));
                        mc.player.getInventory().selectedSlot = prevCurrentItem;
                        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, nearest_slot, SlotActionType.SWAP, mc.player);
                        sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                        if (resetAttackCooldown.getValue()) {
                            mc.player.getItemCooldownManager().set(mc.player.getMainHandStack().getItem(), 0);
                        }
                    }
                }
            } else {
                clickSlot(slot, nearest_slot, SlotActionType.SWAP);
            }
        }
    }

    private void clickSlot(int slot, int nearest_slot, SlotActionType actionType) {
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, nearest_slot, actionType, mc.player);
    }

    private void sendPacket(Packet<?> packet) {
        mc.getNetworkHandler().sendPacket(packet);
    }

    private int findNearestCurrentItem() {
        return mc.player.getInventory().selectedSlot;
    }

    private int getItemSlot() {
        return -1;
    }
}
