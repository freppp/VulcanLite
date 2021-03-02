package me.frap.vulcanlite.command;

import me.frap.vulcanlite.config.Config;
import me.frap.vulcanlite.util.ColorUtil;
import org.bukkit.command.CommandSender;

public class CommandManager {

    public void sendMessage(final CommandSender sender, final String string) {
        sender.sendMessage(ColorUtil.translate(string.replaceAll("%prefix%", ColorUtil.translate(Config.Values.PREFIX))));
    }

    public void sendLine(final CommandSender sender) {
        sendMessage(sender, "&7&m---»--*-------------------------------------*--«---");
    }
}
