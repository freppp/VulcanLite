package me.frep.vulcanlite.check.impl.killaura;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.check.annotation.CheckInfo;
import me.frep.vulcanlite.check.enums.CheckCategory;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

/**
 * @author frep
 * @since 02/09/2021
 *
 * You can't attack more than one unique entity within a single client tick. This check simply
 * enforces that rule by checking whether or not the player attacked an entity with different
 * IDs more than once within a single client tick.
 */

@CheckInfo(name = "Aim", type = "B", complexType = "Multi", category = CheckCategory.COMBAT, description = "Attacked two entities at once")
public class KillAuraB extends Check {

    private int ticks, lastEntityId;

    public KillAuraB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {

            /*
             * Create our Use Entity wrapper from the Packet object.
             */

            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            check: {

                /*
                 * We only want to check if it's an attack packet, not interact or another action.
                 */

                if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) break check;

                /*
                 * You can only attack one unique entity within a single tick (we use flying packets to track ticks
                 * here). If the player attacks an entity with a different ID than the last entity that they attacked
                 * more than once within the same tick, we can flag them. This will pretty much never false ever.
                 */

                if (wrapper.getEntityId() != lastEntityId) {

                    /*
                     * Every unique entity hit we increase this integer; if it's greater than 1, we know that they
                     * attacked two unique entities within the same tick, since this ticks variable is reset every
                     * single flying packet sent by the client.
                     */

                    if (++ticks > 1) {
                        fail();
                    }
                }

                /*
                 * Parse the lastEntityId variable to the entityId from the wrapper.
                 */

                lastEntityId = wrapper.getEntityId();
            }
        } else if (packet.isFlying()) {

            /*
             * Every flying packet (tick) we reset the tick variable to 0.
             */

            ticks = 0;
        }
    }
}
