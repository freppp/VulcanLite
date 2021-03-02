package me.frap.vulcanlite.data.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import lombok.Getter;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.util.MathUtil;
import org.bukkit.World;

@Getter
public class PositionProcessor {

    private final PlayerData data;

    private double x, y, z, lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ, deltaXZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaXZ;

    private boolean clientOnGround, serverOnGround;

    private int sinceFlightTicks, clientGroundTicks, clientAirTicks;

    private World world;

    public PositionProcessor(final PlayerData data) {
        this.data = data;
    }

    /*
     * Parse all of our position and movement data which will be accessed in our checks.
     */

    public void handleFlying(final WrappedPacketInFlying wrapper) {
        clientOnGround = wrapper.isOnGround();

        if (wrapper.isPosition()) {
            world = data.getPlayer().getWorld();

            this.x = wrapper.getX();
            this.y = wrapper.getY();
            this.z = wrapper.getZ();

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

            serverOnGround = y % 0.015625 == 0;

            handlePositionTicks();
        }

        handleFlyingTicks();
    }

    private void handlePositionTicks() {
        if (data.getPlayer().getAllowFlight()) sinceFlightTicks = 0;
        else ++sinceFlightTicks;
    }

    private void handleFlyingTicks() {
        if (clientOnGround) ++clientGroundTicks;
        else clientGroundTicks = 0;

        if (!clientOnGround) ++clientAirTicks;
        else clientAirTicks = 0;
    }
}
