package me.frap.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;

/**
 * @author frap
 * @since 02/08/2021
 *
 * Player's cant interact with themselves - enough said.
 */

@CheckInfo(name = "Protocol", type = "D", complexType = "Interact", category = CheckCategory.PLAYER, description = "Self interact")
public class ProtocolD extends Check {

    public ProtocolD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {

            /*
             * Create the Use Entity wrapper from the Packet object.
             */

            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            /*
             * If the entity ID in the wrapper matches the entity ID of the player, then fail.
             */

            if (wrapper.getEntityId() == data.getPlayer().getEntityId()) {
                fail();
            }
        }
    }
}
