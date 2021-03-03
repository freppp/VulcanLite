package me.frap.vulcanlite.check.manager;

import lombok.Getter;
import me.frap.vulcanlite.check.impl.aim.*;
import me.frap.vulcanlite.check.impl.protocol.*;
import me.frap.vulcanlite.check.impl.speed.SpeedA;
import me.frap.vulcanlite.config.Checks;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.impl.killaura.*;
import me.frap.vulcanlite.data.PlayerData;

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
                KillAuraC.class,
                ProtocolA.class,
                ProtocolB.class,
                ProtocolC.class,
                ProtocolD.class,
                ProtocolE.class,
                ProtocolF.class,
                ProtocolG.class,
                ProtocolH.class,
                ProtocolI.class,
                ProtocolJ.class,
                SpeedA.class,
        };
    }

    public static List<Check> loadChecks(final PlayerData data) {
        final List<Check> checks = new ArrayList<>();
        for (final Constructor<?> constructor : CONSTRUCTORS) {
            try {
                checks.add((Check) constructor.newInstance(data));
            } catch (final Exception exception) {
                System.err.println("Failed to load checks for " + data.getPlayer().getName());
                exception.printStackTrace();
            }
        }
        return checks;
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
