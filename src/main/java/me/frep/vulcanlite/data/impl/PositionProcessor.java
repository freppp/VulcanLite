package me.frep.vulcanlite.data.impl;

import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.util.MathUtil;

public class PositionProcessor {

    private final PlayerData data;

    private double x, y, z, lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ, deltaXZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaXZ;

    public PositionProcessor(final PlayerData data) {
        this.data = data;
    }

    /*
     * Parse all of our position and movement data which will be accessed in our checks.
     */

    public void handle(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        deltaX = x - lastX;
        deltaY = y - lastY;
        deltaZ = z - lastZ;
        deltaXZ = MathUtil.magnitude(deltaX, deltaZ);

        lastX = x;
        lastY = y;
        lastZ = z;

        lastDeltaX = deltaX;
        lastDeltaY = deltaY;
        lastDeltaZ = deltaZ;
        lastDeltaXZ = deltaXZ;
    }
}
