package me.frep.vulcanlite.data.impl;

import me.frep.vulcanlite.data.PlayerData;

public class PositionProcessor {

    private final PlayerData data;

    private double x, y, z, lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ, deltaXZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaXZ;

    public PositionProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handle(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        deltaX = x - lastX;
        deltaY = y - lastY;
        deltaZ = z - lastZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        lastDeltaX = deltaX;
        lastDeltaY = deltaY;
        lastDeltaZ = deltaZ;
    }
}
