package me.frap.vulcanlite.listener;

import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        VulcanLite.INSTANCE.getPlayerDataManager().create(player);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        VulcanLite.INSTANCE.getPlayerDataManager().remove(player);
    }
}
