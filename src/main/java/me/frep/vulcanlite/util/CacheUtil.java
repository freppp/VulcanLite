package me.frep.vulcanlite.util;

import lombok.experimental.UtilityClass;
import me.frep.vulcanlite.check.annotation.CheckInfo;
import me.frep.vulcanlite.check.manager.CheckManager;
import me.frep.vulcanlite.config.Checks;

import java.util.List;

@UtilityClass
public class CacheUtil {

    public void resetCheckValues() {
        Checks.Values.ENABLED_CHECKS.clear();
        Checks.Values.PUNISHABLE.clear();
        Checks.Values.BROADCAST_PUNISHMENT.clear();

        Checks.Values.ALERT_INTERVAL.clear();
        Checks.Values.MAX_VIOLATIONS.clear();

        Checks.Values.PUNISHMENT_COMMANDS.clear();
    }

    public void cacheCheckValues() {
        resetCheckValues();
        for (final Class clazz : CheckManager.CHECKS) {
            final String className = clazz.getSimpleName();

            final CheckInfo checkInfo = (CheckInfo) clazz.getAnnotation(CheckInfo.class);

            String checkCategory = "";
            switch (checkInfo.category()) {
                case COMBAT:
                    checkCategory = "combat";
                    break;
                case MOVEMENT:
                    checkCategory = "movement";
                    break;
                case PLAYER:
                    checkCategory = "player";
                    break;
            }

            final String checkName = checkInfo.name().toLowerCase().replaceAll(" ", "");
            final String checkType = checkInfo.type().toLowerCase();

            final String path = "checks." + checkCategory + "." + checkName + "." + checkType + ".";

            final boolean enabled = Checks.getBoolean(path + "enabled");
            final boolean punishable = Checks.getBoolean(path + "punishable");
            final boolean broadcastPunishment = Checks.getBoolean(path + "broadcast-punishment");

            final int alertInterval = Checks.getInt(path + "alert-interval");
            final int maxViolations = Checks.getInt(path + "max-violations");

            final List<String> punishmentCommands = Checks.getStringList(path + "punishment-commands");

            Checks.Values.ENABLED_CHECKS.put(className, enabled);
            Checks.Values.PUNISHABLE.put(className, punishable);
            Checks.Values.BROADCAST_PUNISHMENT.put(className, broadcastPunishment);

            Checks.Values.ALERT_INTERVAL.put(className, alertInterval);
            Checks.Values.MAX_VIOLATIONS.put(className, maxViolations);

            Checks.Values.PUNISHMENT_COMMANDS.put(className, punishmentCommands);
        }
    }
}
