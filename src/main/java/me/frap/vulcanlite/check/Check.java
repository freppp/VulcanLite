package me.frap.vulcanlite.check;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.packet.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class Check {

    protected final PlayerData data;

    @Setter
    private int vl;

    private double buffer;

    public Check(final PlayerData data) {
        this.data = data;
    }

    public abstract void handle(final Packet packet);

    protected void fail(final double multiple, final Object info) {
        multiplyBuffer(multiple);

        VulcanLite.INSTANCE.getAlertExecutor().execute(() ->
                VulcanLite.INSTANCE.getAlertManager().handleAlert(data, this, Objects.toString(info)));
    }

    protected void fail() {
        VulcanLite.INSTANCE.getAlertExecutor().execute(() ->
                VulcanLite.INSTANCE.getAlertManager().handleAlert(data, this, ""));
    }

    protected void fail(final Object info) {
        VulcanLite.INSTANCE.getAlertExecutor().execute(() ->
                VulcanLite.INSTANCE.getAlertManager().handleAlert(data, this, Objects.toString(info)));
    }

    protected void fail(final double multiple) {
        multiplyBuffer(multiple);

        VulcanLite.INSTANCE.getAlertExecutor().execute(() ->
                VulcanLite.INSTANCE.getAlertManager().handleAlert(data, this, ""));
    }

    public double increaseBuffer() {
        return buffer = Math.min(10000, buffer + 1);
    }

    public double decreaseBufferBy(final double amount) {
        return buffer = Math.max(0, buffer - amount);
    }

    public void multiplyBuffer(final double multiplier) {
        buffer *= multiplier;
    }

    public void resetBuffer() {
        buffer = 0;
    }

    public int ticks() {
        return VulcanLite.INSTANCE.getTickManager().getTicks();
    }

    public int hitTicks() {
        return data.getCombatProcessor().getHitTicks();
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
