package me.frap.vulcanlite.check.impl.protocol;

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
 * Checks if a player sends PacketPlayInHeldItemSlot and PacketPlayInBlockPlace in the same tick.
 */

@CheckInfo(name = "Protocol", type = "J", complexType = "Slot Order", category = CheckCategory.PLAYER, description = "Sent HeldItemSlot while placing.")
public class ProtocolJ extends Check {

    public ProtocolJ(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        /*
         * Making sure the packet is a Held Item Slot.
         */

        if (packet.isHeldItemSlot()) {

            /*
             * Get the placing variable from the Action Processor. This variable gets reset to false every flying packet
             * sent by the client, so we can check if they are sending a Held Item Slot packet while this variable
             * while this is true, which wouldn't be possible to a Vanilla client.
             */

            final boolean placing = data.getActionProcessor().isPlacing();

            /*
             * We need to exempt if the client or server version is higher than 1.9 since there aren't any Flying
             * packets when standing still.
             */

            final boolean exempt = isExempt(ExemptType.SERVER_VERSION, ExemptType.CLIENT_VERSION);

            check: {

                /*
                 * If they are placing and aren't exempt, then fail.
                 */

                if (!placing || exempt) break check;

                fail();
            }
        }
    }
}
