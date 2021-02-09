package me.frep.vulcanlite.check.impl.protocol;

import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

/**
 * @author frep
 * @since 02/08/2021
 *
 * In Minecraft, the player's pitch is always clamped between -90 and 90.
 */

public class ProtocolA extends Check {

    public ProtocolA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {

            /*
             * Get the pitch from the Rotation Processor.
             */

            final float pitch = data.getRotationProcessor().getPitch();

            /*
             * Pitch within the game is always clamped between 90 and -90, no exceptions (unless using some
             * broken client, who knows). If the absolute value of the pitch is greater than 90, flag.
             */

            if (Math.abs(pitch) > 90) {
                fail("pitch=" + pitch);
            }
        }
    }
}
