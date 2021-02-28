package me.frap.vulcanlite.alert;

import lombok.Getter;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class AlertManager {

    private final Set<UUID> alertsEnabled = new HashSet<>();

    public void toggleAlerts(final Player player) {
        if (alertsEnabled.contains(player.getUniqueId())) {
            alertsEnabled.remove(player.getUniqueId());
            // Alerts disabled!
        } else {
            alertsEnabled.add(player.getUniqueId());
            // Alerts enabled!
        }
    }

    public void handleAlert(final PlayerData data, final Check check, final String info) {
        
    }
}
