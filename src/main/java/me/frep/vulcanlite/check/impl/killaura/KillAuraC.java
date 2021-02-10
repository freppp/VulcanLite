package me.frep.vulcanlite.check.impl.killaura;

import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.check.annotation.CheckInfo;
import me.frep.vulcanlite.check.enums.CheckCategory;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;
import me.frep.vulcanlite.util.PlayerUtil;
import org.bukkit.entity.Player;

/**
 * @author frep
 * @since 02/10/2021
 *
 * A very simple, yet very effective, Keep Sprint check. We are just making sure that the player
 * decelerates after they attack a player. If not, it's a strong indication they're using Kill Aura
 * with Keep Sprint (this should flag a lot of auras, since using aura without keep sprint is no fun :().
 */

@CheckInfo(name = "Kill Aura", type = "C", complexType = "Keep Sprint", category = CheckCategory.COMBAT, description = "Not decelerating after attacking")
public class KillAuraC extends Check {

    public KillAuraC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {

        /*
         * If it is a position packet and the player attacked within the last 3 ticks.
         */

        if (packet.isPosition() && hitTicks() < 3) {

            /*
             * Grab our current and last deltaXZ's from the Position Processor.
             */

            final double deltaXZ = data.getPositionProcessor().getDeltaXZ();
            final double lastDeltaXZ = data.getPositionProcessor().getLastDeltaXZ();

            /*
             * Calculate the acceleration (change between current and last deltaXZ).
             */

            final double acceleration = Math.abs(deltaXZ - lastDeltaXZ);

            /*
             * Get whether or not the player is actually sprinting.
             */

            final boolean sprinting = data.getActionProcessor().isSprinting();

            /*
             * Make sure that the last entity the player attacked was a player, since you
             * only decelerate when attacking a player and not any other mobs.
             */

            final boolean target = data.getCombatProcessor().getTarget() != null &&
                    data.getCombatProcessor().getTarget() instanceof Player;

            /*
             * We make this variable to ensure that the player is actually moving at a speed
             * that is around what it would be when they are sprinting, since it's possible
             * to have a very low acceleration, such as when someone clicks really fast against
             * a player. Simply checking their deltaXZ and making sure it's greater than this will
             * filter out any small false positives that result because of that.
             */

            final double baseSpeed = PlayerUtil.getBaseSpeed(data.getPlayer(), .25);

            /* Construct our invalid statement from all of the conditions we have. .0025 is sort of a
             * magic value, some people use .0027, but I have found this value to work well and still
             * quickly flag without any false positives.
             */

            final boolean invalid = acceleration < .0025 && sprinting && target && deltaXZ > baseSpeed;

            /*
             * If all of the conditions are meant, increase the buffer.
             */

            if (invalid) {

                /*
                 * Slightly bigger buffer than usual here just to make sure there aren't any false positives.
                 * Getting above even 2 or 3 buffer should even be pretty difficult for a legit player.
                 */

                if (increaseBuffer() > 5) {

                    /*
                     * If the buffer is greater than 5, multiply it by .5 and flag.
                     */

                    fail(.5, "acceleration=" + acceleration + " deltaXZ=" + deltaXZ);
                }
            } else {

                /*
                 * If they didn't meet the conditions, then we decrease the buffer.
                 */

                decreaseBufferBy(.45);
            }
        }
    }
}
