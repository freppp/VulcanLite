package me.frap.vulcanlite.check;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.check.annotation.CheckInfo;
import me.frap.vulcanlite.check.enums.CheckCategory;
import me.frap.vulcanlite.config.Checks;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.exempt.type.ExemptType;
import me.frap.vulcanlite.packet.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class Check {

    protected final PlayerData data;

    private final String className = getClass().getSimpleName();

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

    protected boolean isExempt(final ExemptType exemptType) {
        return data.getExemptProcessor().isExempt(exemptType);
    }

    protected boolean isExempt(final ExemptType... exemptTypes) {
        return data.getExemptProcessor().isExempt(exemptTypes);
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

    public String getName() {
        return getCheckInfo().name();
    }

    public String getComplexType() {
        return getCheckInfo().complexType();
    }

    public String getType() {
        return getCheckInfo().type();
    }

    public CheckCategory getCheckCategory() {
        return getCheckInfo().category();
    }

    public CheckInfo getCheckInfo() {
        if (getClass().isAnnotationPresent(CheckInfo.class)) {
            return getClass().getAnnotation(CheckInfo.class);
        } else {
            System.err.println("CheckInfo annotation hasn't been added to the class " + getClass().getSimpleName() + "!");
        }
        return null;
    }

    public int getMaxVl() {
        return Checks.Values.MAX_VIOLATIONS.get(className);
    }


    public int hitTicks() {
        return data.getCombatProcessor().getHitTicks();
    }


    public long now() {
        return System.currentTimeMillis();
    }

    public int getAlertInterval() {
        return Checks.Values.ALERT_INTERVAL.get(className);
    }
}
