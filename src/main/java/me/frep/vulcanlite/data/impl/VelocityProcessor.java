package me.frep.vulcanlite.data.impl;

import lombok.Getter;
import me.frep.vulcanlite.VulcanLite;
import me.frep.vulcanlite.data.PlayerData;
import me.frep.vulcanlite.util.MathUtil;

@Getter
public class VelocityProcessor {

    private final PlayerData data;

    private double velocityX, velocityY, velocityZ, velocityXZ;

    private int lastVelocity;

    public VelocityProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handle(final double velocityX, final double velocityY, final double velocityZ) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        velocityXZ = MathUtil.magnitude(velocityX, velocityZ);

        lastVelocity = VulcanLite.INSTANCE.getTickManager().getTicks();
    }
}
