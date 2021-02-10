package me.frap.vulcanlite.exempt;

import lombok.RequiredArgsConstructor;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.exempt.type.ExemptType;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author Elevated
 * @since 01/19/2021
 */

@RequiredArgsConstructor
public final class ExemptProcessor {

    private final PlayerData data;

    /**
     *
     * @param exceptType - The type of exception you want return.
     * @return - True/False depending on appliance.
     */

    public boolean isExempt(final ExemptType exceptType) {
        return exceptType.getException().apply(data);
    }

    /**
     *
     * @param exceptTypes - An array of possible exceptions.
     * @return - True/False depending on if any match the appliance.
     */

    public boolean isExempt(final ExemptType... exceptTypes) {
        return Arrays.stream(exceptTypes).anyMatch(this::isExempt);
    }

    /**
     *
     * @param exception - A custom function-based exception
     * @return - True/False depending on appliance.
     */

    public boolean isExempt(final Function<PlayerData, Boolean> exception) {
        return exception.apply(data);
    }
}
