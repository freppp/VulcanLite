package me.frep.vulcanlite.util;

import com.google.gson.internal.$Gson$Preconditions;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ServerUtil {

    public ServerVersion getServerVersion() {
        return PacketEvents.get().getServerUtils().getVersion();
    }

    public boolean isHigherThan1_9() {
        return getServerVersion().isHigherThanOrEquals(ServerVersion.v_1_9);
    }

    public boolean isHigherThan1_13() {
        return getServerVersion().isHigherThanOrEquals(ServerVersion.v_1_13);
    }
}
