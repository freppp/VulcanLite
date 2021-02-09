package me.frep.vulcanlite.packet.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

public class SendingPacketProcessor {

    public void handle(final PlayerData data, final Packet packet) {
        if (packet.isVelocity()) {
            final WrappedPacketOutEntityVelocity wrapper = new WrappedPacketOutEntityVelocity(packet.getRawPacket());

            if (wrapper.getEntityId() == data.getPlayer().getEntityId()) {
                data.getVelocityProcessor().handle(wrapper.getVelocityX(), wrapper.getVelocityY(), wrapper.getVelocityZ());
            }
        }
        if (packet.isTeleport()) {
            final WrappedPacketOutPosition wrapper = new WrappedPacketOutPosition(packet.getRawPacket());


        }
    }
}
