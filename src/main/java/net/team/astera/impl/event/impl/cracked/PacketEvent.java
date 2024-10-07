package net.team.astera.impl.event.impl.cracked;

import net.team.astera.impl.event.Event;
import net.minecraft.network.packet.Packet;

public abstract class PacketEvent extends Event {

    public final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    /*public Packet<?> getPacket() {
        return packet;
    }*/
    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

}