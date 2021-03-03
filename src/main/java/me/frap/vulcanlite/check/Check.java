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

@Getter @Setter
public abstract class Check {

    protected final PlayerData data;

    private final String className;

    private final CheckInfo checkInfo;

    private int vl;

    private double buffer;

    public Check(final PlayerData data) {
        this.data = data;

        if (getClass().isAnnotationPresent(CheckInfo.class)) {
            this.checkInfo = getClass().getAnnotation(CheckInfo.class);
        } else {
            System.err.println("CheckInfo annotation was not found for the class " + getClass().getSimpleName() +"!");
            this.checkInfo = null;
        }

        this.className = getClass().getSimpleName();
    }

    public abstract void handle(final Packet packet);

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

    protected void fail(final double multiple, final Object info) {
        multiplyBuffer(multiple);

        VulcanLite.INSTANCE.getAlertExecutor().execute(() ->
                VulcanLite.INSTANCE.getAlertManager().handleAlert(data, this, Objects.toString(info)));
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

    public String getName() {
        return checkInfo.name();
    }

    public String getComplexType() {
        return checkInfo.complexType();
    }

    public String getType() {
        return checkInfo.type();
    }

    public CheckCategory getCheckCategory() {
        return checkInfo.category();
    }

    public int getMaxVl() {
        return Checks.Values.MAX_VIOLATIONS.get(className);
    }

    public int hitTicks() {
        return data.getCombatProcessor().getHitTicks();
    }

    public int getAlertInterval() {
        return Checks.Values.ALERT_INTERVAL.get(className);
    }

    public String getConfigFriendlyName() {
        return getName().toLowerCase().replaceAll(" ", "");
    }

    public String getConfigFriendlyType() {
        return getType().toLowerCase();
    }
}
