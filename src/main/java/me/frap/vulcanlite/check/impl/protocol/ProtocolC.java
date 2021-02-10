package me.frap.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;
import me.frap.vulcanlite.util.ServerUtil;

/**
 * @author frap
 * @since 02/08/2021
 *
 * Checks for when hacked clients send fake/spoofed Abilities packets - used by some disablers
 * and fly modules.
 */

@CheckInfo(name = "Protocol", type = "C", complexType = "Abilities", category = CheckCategory.PLAYER, description = "Spoofed Abilities packets")
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
             * Whether or not the player is actually allowed to fly. We don't want to run the check if they are.
             */

            final boolean allow = data.getPlayer().getAllowFlight();

            if (allow) return;

            high: {
                if (!ServerUtil.isHigherThan1_9()) break high;

                /*
                 * The wrapper is a little different above 1.9, so we just fail here.
                 */

                fail();
            }

            low: {
                if (ServerUtil.isHigherThan1_9()) break low;

                /*
                 * WrappedPacketInAbilities#isFlightAllowed only works on lower than 1.9.
                 */

                if (wrapper.isFlightAllowed()) fail();
            }
        }
    }
}
