package me.frep.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;
import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

/**
 * @author frep
 * @since 02/08/2021
 *
 * Checks for when hacked clients send fake/spoofed Abilities packets - used by some disablers
 * and fly modules.
 */

public class ProtocolC extends Check {

    public ProtocolC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isAbilities()) {

            /*
             * Create our Abilities wrapper from the Packet object.
             */

            final WrappedPacketInAbilities wrapper = new WrappedPacketInAbilities(packet.getRawPacket());

            /*
             * Whether or not the player is actually allowed to fly.
             */

            final boolean allow = data.getPlayer().getAllowFlight();

            check: {

                /*
                 * If the player is actually allowed to fly, break, as we don't want to check anything in that case.
                 */

                if (allow) break check;


            }
        }
    }
}
