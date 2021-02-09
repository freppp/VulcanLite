package me.frep.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

/**
 * @author frep
 * @since 02/08/2021
 *
 * The client has to send a position update once every 20 ticks, even if the player isn't moving.
 */

public class ProtocolB extends Check {

    private int ticks;

    public ProtocolB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {

            /*
             * Create our Flying wrapper from the Packet object.
             */

            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            /*
             * If the wrapper is a Position or PositionLook, or the player is inside of a vehicle,
             * reset the ticks variable (since there was a position update). You won't send any
             * flying packets in vehicles, there is the Steer Vehicle packet for that, so we don't
             * want to be checking while in vehicles.
             */

            if (wrapper.isPosition() || data.getPlayer().isInsideVehicle()) {
                ticks = 0;
                return;
            }

            /*
             * If the ticks variable has been incremented more than 20 times in a row, then flag. If the
             * player did send a position update, then it would be set to 0 above, and this counter would be reset.
             * Many cheats don't follow the protocol and will send way more than 20 flying packets in a row, such as
             * some Regen modules or FastBow.
             */

            if (++ticks > 20) {
                fail("ticks=" + ticks);
            }
        } else if (packet.isSteerVehicle()) {

            /*
             * Probably not needed, but doesn't hurt to check here just to be safe.
             */

            ticks = 0;
        }
    }
}
