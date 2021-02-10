package me.frap.vulcanlite.data;

import lombok.Getter;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.check.manager.CheckManager;
import me.frap.vulcanlite.data.impl.*;
import me.frap.vulcanlite.data.impl.*;
import me.frap.vulcanlite.exempt.ExemptProcessor;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class PlayerData {

    private final Player player;

    private final RotationProcessor rotationProcessor = new RotationProcessor(this);
    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final VelocityProcessor velocityProcessor = new VelocityProcessor(this);
    private final ExemptProcessor exemptProcessor = new ExemptProcessor(this);
    private final CombatProcessor combatProcessor = new CombatProcessor(this);
    private final ActionProcessor actionProcessor = new ActionProcessor(this);

    private final List<Check> checks = CheckManager.loadChecks(this);

    public PlayerData(final Player player) {
        this.player = player;
    }
}
