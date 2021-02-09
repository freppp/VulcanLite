package me.frep.vulcanlite.packet.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

public class ReceivingPacketProcessor {

    public void handle(final PlayerData data, final Packet packet) {
        if (packet.isFlying()) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            position: {
                if (!wrapper.isPosition()) break position;

                data.getPositionProcessor().handle(wrapper.getX(), wrapper.getY(), wrapper.getZ());
            }

            look: {
                if (!wrapper.isLook()) break look;

                data.getRotationProcessor().handle(wrapper.getYaw(), wrapper.getPitch());
            }
        }


    }
}
