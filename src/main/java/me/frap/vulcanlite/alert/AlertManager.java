package me.frap.vulcanlite.alert;

import lombok.Getter;
import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.check.Check;
import me.frap.vulcanlite.config.Checks;
import me.frap.vulcanlite.config.Config;
import me.frap.vulcanlite.data.PlayerData;
import me.frap.vulcanlite.util.ColorUtil;
import me.frap.vulcanlite.util.PlayerUtil;
import me.frap.vulcanlite.util.ServerUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

@Getter
public class AlertManager {

    /*
     * You could use UUID here instead of Player but I don't like running Bukkit#getPlayer every alert so
     * this should be a little lighter.
     */
    private final Set<Player> alertsEnabled = new HashSet<>();

    public void toggleAlerts(final Player player) {
        if (alertsEnabled.contains(player)) {
            alertsEnabled.remove(player);
            player.sendMessage("alerts disabled!");
        } else {
            alertsEnabled.add(player);
            player.sendMessage("alerts enabled");
        }
    }

    public void handleAlert(final PlayerData data, final Check check, final String info) {
        check.setVl(check.getVl() + 1);

        final int vl = check.getVl();

        console: {
            if (!Config.Values.ALERTS_TO_CONSOLE) break console;

            System.out.println(ColorUtil.translate(Config.Values.ALERTS_FORMAT)
                    .replaceAll("%player%", data.getPlayer().getName())
                    .replaceAll("%info%", info)
                    .replaceAll("%check%", check.getName())
                    .replaceAll("%type%", check.getType())
                    .replaceAll("%tps%", Double.toString(ServerUtil.getTps()))
                    .replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate("*") : "")
                    .replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(data.getPlayer())))
                    .replaceAll("%vl%", Integer.toString(vl)));
        }

        final int alertInterval = check.getAlertInterval();

        alert: {
            if (alertsEnabled.isEmpty() || vl % alertInterval != 0) break alert;

            final TextComponent alertMessage = new TextComponent(ColorUtil.translate(Config.Values.ALERTS_FORMAT)
                    .replaceAll("%player%", data.getPlayer().getName())
                    .replaceAll("%check%", check.getName())
                    .replaceAll("%vl%", Integer.toString(vl))
                    .replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate("*") : "")
                    .replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(data.getPlayer())))
                    .replaceAll("%tps%", Double.toString(ServerUtil.getTps()))
                    .replaceAll("%type%", check.getType()));

            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    Config.Values.ALERTS_CLICK_COMMAND.replaceAll("%player%", data.getPlayer().getName())));

            final StringBuilder stringBuilder = new StringBuilder();
            int listSize = Config.Values.ALERTS_HOVER_MESSAGE.size();
            int i = 1;

            for (final String hoverMessages : Config.Values.ALERTS_HOVER_MESSAGE) {
                if (i == listSize) {
                    stringBuilder.append(hoverMessages);
                } else {
                    stringBuilder.append(hoverMessages).append("\n");
                }
                i++;
            }

            final String hoverMessage = ColorUtil.translate(stringBuilder.toString()
                    .replaceAll("%player%", data.getPlayer().getName())
                    .replaceAll("%check%", check.getName())
                    .replaceAll("%vl%", Integer.toString(vl))
                    .replaceAll("%complex-type%", check.getCheckInfo().complexType())
                    .replaceAll("%version%", PlayerUtil.getClientVersionToString(data.getPlayer()))
                    .replaceAll("%ping%", Integer.toString(PlayerUtil.getPing(data.getPlayer())))
                    .replaceAll("%description%", check.getCheckInfo().description())
                    .replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate("*") : "")
                    .replaceAll("%type%", check.getType())
                    .replaceAll("%tps%", Double.toString(ServerUtil.getTps()))
                    .replaceAll("%info%", info)
                    .replaceAll("%check%", check.getCheckInfo().name()));

            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));

            if (Config.Values.ASYNC_ALERTS) {
                alertsEnabled.forEach(player -> player.spigot().sendMessage(alertMessage));
            } else {
                Bukkit.getScheduler().runTask(VulcanLite.INSTANCE.getPlugin(), () ->
                        alertsEnabled.forEach(player -> player.spigot().sendMessage(alertMessage)));
            }
        }

        punish: {
            if (vl < check.getMaxVl() || check.getCheckInfo().experimental()) break punish;

            VulcanLite.INSTANCE.getPunishmentManager().handlePunishment(data, check);
        }
    }
}
