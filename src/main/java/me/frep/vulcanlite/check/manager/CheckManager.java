package me.frep.vulcanlite.check.manager;

import lombok.Getter;
import me.frep.vulcanlite.check.Check;
import me.frep.vulcanlite.check.impl.aim.AimA;
import me.frep.vulcanlite.check.impl.killaura.KillAuraA;
import me.frep.vulcanlite.check.impl.killaura.KillAuraB;
import me.frep.vulcanlite.check.impl.protocol.*;
import me.frep.vulcanlite.config.Checks;
import me.frep.vulcanlite.data.PlayerData;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class CheckManager {

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    @Getter
    public static final Class<? extends Check>[] CHECKS;

    static {
        CHECKS = new Class[]{
                AimA.class,
                KillAuraA.class,
                KillAuraB.class,
                ProtocolA.class,
                ProtocolB.class,
                ProtocolC.class,
                ProtocolD.class,
                ProtocolE.class,
                ProtocolF.class,
        };
    }

    private final List<Check> checks = new ArrayList<>();

    public static List<Check> loadChecks(final PlayerData data) {
        final List<Check> checkList = new ArrayList<>();
        for (final Constructor<?> constructor : CONSTRUCTORS) {
            try {
                checkList.add((Check) constructor.newInstance(data));
            } catch (final Exception exception) {
                System.err.println("Failed to load checks for " + data.getPlayer().getName());
                exception.printStackTrace();
            }
        }
        return checkList;
    }

    public static void setup() {
        for (final Class<? extends Check> clazz : CHECKS) {
            if (Checks.Values.ENABLED_CHECKS.get(clazz.getSimpleName())) {
                try {
                    CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
                } catch (final NoSuchMethodException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
