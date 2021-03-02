package me.frap.vulcanlite.check.impl.speed;

import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;

/**
 * @author frap
 * @since 03/02/2021
 *
 * A simple air friction check which simply enforces the game mechanics.
 */

@CheckInfo(name = "Speed", type = "A", complexType = "Friction", category = CheckCategory.MOVEMENT, description = "Invalid air friction.")
public class SpeedA extends Check {

    public SpeedA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        /*
         * Check if the packet is a Position.
         */

        if (packet.isPosition()) {

            /*
             * Get the current and last deltaXZ values from the Position Processor.
             */

            final double deltaXZ = data.getPositionProcessor().getDeltaXZ();
            final double lastDeltaXZ = data.getPositionProcessor().getLastDeltaXZ();

            /*
             * Get the sprinting variable from the Action Processor.
             */

            final boolean sprinting = data.getActionProcessor().isSprinting();
        }
    }
}
