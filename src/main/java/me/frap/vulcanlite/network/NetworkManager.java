package me.frap.vulcanlite.network;

import com.google.common.collect.ImmutableSet;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;

public class NetworkManager extends PacketListenerDynamic {

    /*
     * We pretty much only listen to these two outbound packets, so we have a 'whitelist' to avoid having to loop through
     * all of the checks and swap threads for packets that we don't even use. The server sends a LOT of packets, so this
     * is pretty helpful in helping performance and reducing CPU usage.
     */

    private final static ImmutableSet<Byte> SEND_WHITELIST = new ImmutableSet.Builder<Byte>()
            .add(PacketType.Play.Server.POSITION).add(PacketType.Play.Server.ENTITY_VELOCITY).build();

    public NetworkManager(final PacketEventPriority priority) {
        super(priority);
    }

    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {

        /*
         * We don't have a whitelist here like we do for outbound packets - we probably could but the client doesn't
         * send nearly as many packets as the server does and we use almost everything that the client sends.
         */

        final PlayerData data = VulcanLite.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());

        handle: {

            /*
             * We don't want to run anything if the data is null, obviously. This should almost never happen, but
             * it doesn't hurt to check, right?
             */

            if (data == null) break handle;

            /*
             * We pass everything into our own thread to avoid running all of our calculations on the Netty thread
             * as they can be quite heavy and bog everything down, such as ping calculations, etc.
             */

            VulcanLite.INSTANCE.getPacketExecutor().execute(() -> VulcanLite.INSTANCE.getReceivingPacketProcessor()
                    .handle(data, new Packet(Packet.Direction.RECEIVE, event.getNMSPacket(), event.getPacketId())));
        }
    }

    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent event) {

        /*
         * We return if the packet isn't even going to be used at all to prevent unnecessary object creation
         * and handling the packet at all.
         */

        if (!SEND_WHITELIST.contains(event.getPacketId())) return;

        final PlayerData data = VulcanLite.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());

        handle: {

            /*
             * We don't want to run anything if the data is null, obviously. This should almost never happen, but
             * it doesn't hurt to check, right?
             */

            if (data == null) break handle;

            /*
             * We pass everything into our own thread to avoid running all of our calculations on the Netty thread
             * as they can be quite heavy and bog everything down, such as ping calculations, etc.
             */

            VulcanLite.INSTANCE.getPacketExecutor().execute(() -> VulcanLite.INSTANCE.getSendingPacketProcessor()
                    .handle(data, new Packet(Packet.Direction.SEND, event.getNMSPacket(), event.getPacketId())));
        }
    }
}
