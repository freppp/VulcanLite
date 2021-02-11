package me.frap.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;
import me.frap.vulcanlite.util.PlayerUtil;

/**
 * @author frap
 * @since 02/10/2021
 *
 * You can't send PacketPlayInBlockPlace and PacketPlayInUseEntity without a flying packet in between.
 */

@CheckInfo(name = "Protocol", type = "F", complexType = "Auto Block", category = CheckCategory.PLAYER, description = "Checks for Auto Block.")
public class ProtocolG extends Check {

    public ProtocolG(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {

            /*
             * Create a Use Entity wrapper from the packet object.
             */

            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            check: {

                /*
                 * We only want to check for attack packets, not interact or something else.
                 */

                if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) break check;


                /*
                 * Get our placing variable from the Action Processor. This gets reset every flying packet,
                 * after a block place packet is sent, so we can check if they are sending this Use Entity
                 * packet while the placing variable is set to true.
                 */

                final boolean placing = data.getActionProcessor().isPlacing();

                /*
                 * Make sure the player is actually holding a sword. I would use Material enums but cross version :(.
                 */

                final boolean sword = PlayerUtil.isHoldingSword(data.getPlayer());

                /*
                 * If the conditions are met, increase the buffer.
                 */

                if (placing && sword) {

                    /*
                     * We don't really need a buffer here because it's just packet order, but better
                     * safe than sorry since who knows what can happen when people use stupid clients.
                     */

                    if (increaseBuffer() > 2) {

                        /*
                         * If the buffer is greater than 2, fail and multiply it by .5.
                         */

                        fail(.5);
                    }
                } else {

                    /*
                     * If they don't meet the conditions, then decrease the buffer.
                     */

                    decreaseBufferBy(.5);
                }
            }
        }
    }
}
