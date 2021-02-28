package me.frap.vulcanlite.packet.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;

public class ReceivingPacketProcessor {

    public void handle(final PlayerData data, final Packet packet) {
        if (packet.isFlying()) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            look: {
                if (!wrapper.isLook()) break look;

                data.getRotationProcessor().handle(wrapper.getYaw(), wrapper.getPitch());
            }

            data.getPositionProcessor().handleFlying(wrapper);

            data.getVelocityProcessor().handleFlying();
            data.getCombatProcessor().handleFlying();
            data.getActionProcessor().handleFlying();
        }

        if (packet.isBlockPlace()) {
            data.getActionProcessor().handleBlockPlace();;
        }

        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            data.getCombatProcessor().handleUseEntity(wrapper);
        }

        if (packet.isEntityAction()) {
            final WrappedPacketInEntityAction wrapper = new WrappedPacketInEntityAction(packet.getRawPacket());

            data.getActionProcessor().handleEntityAction(wrapper);
        }

        /*
         * Loop through all of our checks and handle the packet.
         */

        data.getChecks().forEach(check -> check.handle(packet));
    }
}
