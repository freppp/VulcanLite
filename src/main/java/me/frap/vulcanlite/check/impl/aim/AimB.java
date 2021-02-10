package me.frap.vulcanlite.check.impl.aim;

import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;

/**
 * @author frap
 * @since 02/09/2021
 *
 * Rotations should be somewhat linear. Checks for when the deltaYaw is exponentially small
 * in comparison to the deltaPitch. (This is pretty much the same as AimA but just inversed
 * since I don't like putting multiple fails within a single check, in case one falses).
 */

@CheckInfo(name = "Aim", type = "B", complexType = "Ratio", category = CheckCategory.COMBAT, description = "Invalid rotation ratio")
public class AimB extends Check {

    public AimB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        /*
         * If the packet is a rotation (Look/PositionLook) and the player attacked within the last 5 ticks.
         */

        if (packet.isRotation() && hitTicks() < 5) {

            /*
             * Grab the Yaw and Pitch deltas from the Rotation Processor.
             */

            final float deltaYaw = data.getRotationProcessor().getDeltaYaw();
            final float deltaPitch = data.getRotationProcessor().getDeltaPitch();

            /*
             * The logic behind this is that a player's rotations should be somewhat linear. You can't move your
             * mouse an exponentially small amount on one axis and a large amount on another. Some aimbots, killauras,
             * and even aim assists fail this. We really don't even need to account for Cinematic or Optifine here
             * since with those, both of the rotations will be exponentially small, not just one. I've seen some
             * Kill Auras that go as far as deltaYaw 1e-20 or something ridiculous, so this can flag those very fast.
             */

            final boolean invalid = deltaPitch > .5F && deltaYaw < .0001 && deltaYaw > 0;

            /*
             * Small little buffer to filter out any random false positives that may happen.
             */

            if (invalid) {
                if (increaseBuffer() > 4) {

                    /*
                     * If the buffer is greater than 4, fail and multiply the buffer by .5
                     */

                    fail(.5, "deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
                }
            } else {

                /*
                 * If they don't meet the conditions, decrease the buffer.
                 */

                decreaseBufferBy(.25);
            }
        }
    }
}
