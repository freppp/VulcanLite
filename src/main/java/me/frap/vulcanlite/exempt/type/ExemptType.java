package me.frap.vulcanlite.exempt.type;

import io.github.retrooper.packetevents.utils.player.ClientVersion;
import lombok.Getter;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.util.PlayerUtil;
import me.frap.vulcanlite.util.ServerUtil;
import org.bukkit.entity.Player;

import java.util.function.Function;

@Getter
public enum ExemptType {

    /*
     * Returns true if the player is allowed to fly or was allowed to fly within the last 60 ticks.
     */

    ALLOW_FLIGHT(data -> data.getPlayer().getAllowFlight() || data.getPositionProcessor().getSinceFlightTicks() < 60),

    /*
     * Returns true if the player's client version is above 1.9 since PacketPlayInFlying isn't sent when the player is
     * standing still in any versions this. We also exempt if it is unresolved to prevent any issues.
     */

    CLIENT_VERSION(data -> PlayerUtil.getClientVersion(data.getPlayer()) != null && (PlayerUtil.getClientVersion(data.getPlayer()).equals(ClientVersion.UNRESOLVED) ||
            PlayerUtil.getClientVersion(data.getPlayer()).isHigherThanOrEquals(ClientVersion.v_1_9))),

    /*
     * Returns true if the server's version is higher for 1.9 as these versions break some packet order checks,
     * again because of the lack of flying packets.
     */

    SERVER_VERSION(data -> ServerUtil.isHigherThan1_9()),

    /*
     * Returns true if the player's Y level is less than 4.
     */

    VOID(data -> data.getPositionProcessor().getY() < 4);

    private final Function<PlayerData, Boolean> exception;

    ExemptType(final Function<PlayerData, Boolean> exception) {
        this.exception = exception;
    }
}
