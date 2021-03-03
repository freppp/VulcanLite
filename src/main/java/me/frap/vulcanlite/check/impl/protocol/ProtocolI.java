package me.frap.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.exempt.type.ExemptType;
import me.frap.vulcanlite.packet.Packet;

/**
 * @author frap
 * @since 03/02/21
 *
 * You can't send PacketPlayInEntityAction and PacketPlayInUseEntity within the same client tick.
 */

@CheckInfo(name = "Protocol", type = "I", complexType = "Attack Order", category = CheckCategory.PLAYER, description = "Sending Action and Attack at the same time.")
public class ProtocolI extends Check {

    public ProtocolI(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        /*
         * Make sure the packet is a Use Entity packet.
         */

        if (packet.isUseEntity()) {

            /*
             * Create a Use Entity wrapper from the Packet object.
             */

            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            /*
             * Get our sendingAction variable from the Action Processor. This value gets reset back to false every
             * flying packet, so we can check if the client is sending an attack packet in the same tick as an entity
             * action packet.
             */

            final boolean action = data.getActionProcessor().isSendingAction();

            /*
             * We need to exempt if the client or server version is higher than 1.9 since there aren't any Flying
             * packets when standing still.
             */

            final boolean exempt = isExempt(ExemptType.CLIENT_VERSION, ExemptType.SERVER_VERSION);

            handle: {

                /*
                 * We only want to fail the check for attack packets and they are sending an action, so this does it all in one statement.
                 */

                if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK || exempt || !action) break handle;

                fail();
            }
        }
    }
}
