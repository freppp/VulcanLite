package me.frep.vulcanlite.check;

import lombok.Getter;
import lombok.Setter;
import me.frep.vulcanlite.VulcanLite;
import me.frep.vulcanlite.data.PlayerData;

@Getter
public abstract class Check {

    protected final PlayerData data;

    @Setter
    private int vl;

    private double buffer;

    public final String className = getClass().getSimpleName();

    public Check(final PlayerData data) {
        this.data = data;
    }

    public void fail(final Object info) {

    }

    public void fail() {

    }

    public double increaseBuffer() {
        return buffer = Math.min(10000, buffer + 1);
    }

    public double decreaseBufferBy(final double amount) {
        return buffer = Math.max(0, buffer - amount);
    }

    public void resetBuffer() {
        buffer = 0;
    }

    public int ticks() {
        return VulcanLite.INSTANCE.getTickManager().getTicks();
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
