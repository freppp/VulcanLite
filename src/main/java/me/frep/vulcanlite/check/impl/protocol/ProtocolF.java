package me.frep.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;
import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.check.annotation.CheckInfo;
import me.frep.vulcanlite.check.enums.CheckCategory;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

/**
 * @author frep
 * @since 02/08/2021
 *
 * Checks for too large or too small Steer Vehicle packets. Commonly used by disablers.
 */

@CheckInfo(name = "Protocol", type = "F", complexType = "Vehicle", category = CheckCategory.PLAYER, description = "Invalid Steer Vehicle packets.")
public class ProtocolF extends Check {

    public ProtocolF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isSteerVehicle()) {

            /*
             * Create a Steer Vehicle wrapper from the Packet object.
             */

            final WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(packet.getRawPacket());

            /*
             * Read the forward and side values from the packet.
             */

            final float forward = wrapper.getForwardValue();
            final float side = wrapper.getSideValue();

            /*
             * The max value of the forward or side value is -.98 or .98. They can never exceed this.
             * If the forward or side values are invalid, fail.
             */

            final boolean invalid = Math.abs(forward) > .98F || Math.abs(side) > .98F;

            if (invalid) {
                fail("forward=" + forward + " side=" + side);
            }
        }
    }
}
