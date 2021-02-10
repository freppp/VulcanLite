package me.frap.vulcanlite.exempt.type;

import lombok.Getter;
import me.frap.vulcanlite.data.PlayerData;

import java.util.function.Function;

@Getter
public enum ExemptType {

    /*
     * Returns true if the player's Y level is less than 4.
     */

    VOID(data -> data.getPositionProcessor().getY() < 4);

    private final Function<PlayerData, Boolean> exception;

    ExemptType(final Function<PlayerData, Boolean> exception) {
        this.exception = exception;
    }
}
