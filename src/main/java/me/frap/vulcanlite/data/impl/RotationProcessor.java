package me.frap.vulcanlite.data.impl;

import lombok.Getter;
import me.frap.vulcanlite.data.PlayerData;

@Getter
public class RotationProcessor {

    private final PlayerData data;

    private float yaw, pitch, lastYaw, lastPitch,
            deltaYaw, deltaPitch, lastDeltaYaw, lastDeltaPitch;

    public RotationProcessor(final PlayerData data) {
        this.data = data;
    }

    /*
     * Parse all of our rotation data which will be accessed in our checks.
     */

    public void handle(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;

        deltaYaw = Math.abs(yaw - lastYaw);
        deltaPitch = Math.abs(pitch - lastPitch);

        lastYaw = yaw;
        lastPitch = pitch;

        lastDeltaYaw = deltaYaw;
        lastDeltaPitch = deltaPitch;
    }
}
