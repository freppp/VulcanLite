package me.frap.vulcanlite.command.impl;

import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.command.CommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand extends CommandManager implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, "cant execute from console");
            return true;
        }
        final Player player = (Player) sender;
        if (player.hasPermission("vulcanlite.alerts")) {
            VulcanLite.INSTANCE.getAlertManager().toggleAlerts(player);
        } else {
            sendMessage(sender, "No permission!");
            return true;
        }
        return true;
    }
}
