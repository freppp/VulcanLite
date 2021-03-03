package me.frap.vulcanlite.util;

import com.google.gson.internal.$Gson$Preconditions;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerUtil {

    /**
     * @return The server's version
     */

    public ServerVersion getServerVersion() {
        return PacketEvents.get().getServerUtils().getVersion();
    }

    /**
     * @return The server's TPS. We use a Math.min so it doesn't exceed 20 since sometimes it may be like
     * 20.01 or something.
     */

    public double getTps() {
        return Math.min(20, PacketEvents.get().getServerUtils().getTPS());
    }

    public boolean isHigherThan1_9() {
        return getServerVersion().isHigherThanOrEquals(ServerVersion.v_1_9);
    }

    public boolean isHigherThan1_13() {
        return getServerVersion().isHigherThanOrEquals(ServerVersion.v_1_13);
    }
}
