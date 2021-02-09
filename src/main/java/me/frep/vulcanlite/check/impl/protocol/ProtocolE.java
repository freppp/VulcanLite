package me.frep.vulcanlite.check.impl.protocol;

import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;
import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.check.annotation.CheckInfo;
import me.frep.vulcanlite.check.enums.CheckCategory;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.packet.Packet;

/**
 * @author frep
 * @since 02/08/2021
 *
 * Checks for duplicate Held Item Slot packets.
 */

@CheckInfo(name = "Protocol", type = "E", complexType = "Hotbar", category = CheckCategory.PLAYER, description = "Duplicate HeldItemSlot packets")
public class ProtocolE extends Check {

    private int lastSlot = -1;

    public ProtocolE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isHeldItemSlot()) {

            /*
             * Create a Held Item Slot wrapper from the Packet object.
             */

            final WrappedPacketInHeldItemSlot wrapper = new WrappedPacketInHeldItemSlot(packet.getRawPacket());

            final int slot = wrapper.getCurrentSelectedSlot();

            /*
             * You can't send two identical slot changes, or else you just wouldn't send a slot change.
             * Commonly flags Scaffold modules and some other miscellaneous cheats.
             */

            if (slot == lastSlot) {
                fail("slot=" + slot + " lastSlot=" + lastSlot);
            }

            /*
             * Parse the lastSlot variable to the current slot.
             */

            lastSlot = slot;
        }
    }
}
