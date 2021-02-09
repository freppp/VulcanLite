package me.frep.vulcanlite.check.impl.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

/**
 * @author frep
 * @since 02/08/2021
 *
 * A simple, yet very effective Post Kill Aura Check. The delay between PacketPlayInFlying and
 * PacketPlayInUseEntity should always be roughly ~50ms. A lot of older clients don't take this
 * into account and send them within the same tick, which is something we can easily detect.
 */

public class KillAuraA extends Check {

    private long lastFlying;
    private boolean sent;

    public KillAuraA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {

            /*
             * Calculate the delay between the current and last flying packet.
             */

            final long delay = now() - lastFlying;

            /*
             * The boolean 'sent' is set to true when the delay between the FLying and Use Entity packet is less than
             * 10ms (see below).
             */

            if (sent) {

                /*
                 * We only want them to actually fail the check / increase the buffer if the delay between their flying packets is
                 * between 40ms and 100ms as this ensures that there won't be any false positives if they lag out.
                 */

                if (delay > 40L && delay < 100L) {

                    /*
                     * Small buffer just to rule out the possibility of any false positives from who knows what.
                     */

                    if (increaseBuffer() > 3) {

                        /*
                         * If the buffer is greater than 3, alert and multiply it by .5.
                         */

                        fail(.5, "delay=" + delay);
                    }
                } else {

                    /*
                     * Decrease the buffer by a little just in case there were any edge cases.
                     */

                    decreaseBufferBy(.125);
                }

                /*
                 * Reset the boolean to false no matter what happens.
                 */

                sent = false;
            }

            /*
             * Parse the lastFlying variable to the current time.
             */

            lastFlying = now();
        } else if (packet.isUseEntity()) {

            /*
             * Create a UseEntity wrapper from the Packet object.
             */

            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            handle: {

                /*
                 * We only want to check if it's an attack packet, not interact or another action.
                 */

                if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) break handle;

                /*
                 * Calculate the delay between the sending of this Use Entity packet and the last flying packet.
                 */

                final long delay = now() - lastFlying;

                /*
                 * The delay between PacketPlayInFlying and PacketPlayInUseEntity should always be around ~50ms,
                 * however it may vary slightly depending on the connection of the player. If it's less than 10,
                 * it's a strong indication they're using a really bad/old Kill Aura which sends the motion
                 * and attack in the same tick or they lagged very badly (will get filtered out above).
                 */

                if (delay < 10) {
                    sent = true;
                }
            }
        }
    }
}
