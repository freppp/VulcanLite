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
 * This check simply enforces the packet order (at least 1.8). You have to swing before you send an attack packet.
 */

@CheckInfo(name = "Protocol", type = "H", complexType = "Attack Order", category = CheckCategory.PLAYER, description = "Enforces the attack packet order.")
public class ProtocolH extends Check {

    private boolean swung;

    public ProtocolH(final PlayerData data) {
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
             * Exempt if the server or client version is higher than 1.9 since the packet order is different
             * in those versions and there's no flying packets when you stand still.
             */

            final boolean exempt = isExempt(ExemptType.SERVER_VERSION, ExemptType.CLIENT_VERSION);

            check: {

                /*
                 * We only want to fail the check for attack packets and they didn't swing and they aren't exempt
                 * , so this takes care of it all in one nice statement.
                 */

                if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK || exempt || swung) break check;


                /*
                 * If it was an attack packet and the packet beforehand wasn't an Arm Animation, then fail.
                 */

                fail();
            }
        } else if (packet.isArmAnimation()) {

            /*
             * If the packet is an Arm Animation, set this variable to true to indicate an arm animation was actually
             * sent by the client.
             */

            swung = true;
        } else if (packet.isFlying()) {

            /*
             * In 1.8, the order is Swing -> Hit -> Flying, and it will always be this way because of TCP.
             */

            swung = false;
        }
    }
}
