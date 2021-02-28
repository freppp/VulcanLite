package me.frap.vulcanlite.check.impl.groundspoof;

import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.exempt.type.ExemptType;
import me.frap.vulcanlite.packet.Packet;

@CheckInfo(name = "Ground Spoof", type = "A", complexType = "Modulo", category = CheckCategory.MOVEMENT, description = "Spoofed OnGround value.")
public class GroundSpoofA extends Check {

    public GroundSpoofA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {

            /*
             * Get the onGround value from the Flying packet. This can be spoofed by the client.
             */

            final boolean clientGround = data.getPositionProcessor().isClientOnGround();

            /*
             * Get our serverGround variable. This will return true if the player's y-level modulo by 1/64 (0.015625)
             * is 0 since every actual onGround value is evenly divisible by this value.
             */

            final boolean serverGround = data.getPositionProcessor().isServerOnGround();

            /*
             * There are a miscellaneous things that we want to exempt for.
             */

            final boolean exempt = isExempt(ExemptType.ALLOW_FLIGHT);
        }
    }
}
