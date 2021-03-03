package me.frap.vulcanlite.event;

import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class EventProcessor implements Listener {

    /*
     * In later versions of the game, you can hook and catch players with fishing rods and send them flying. There
     * are definitely better ways to account for this but this is the easiest.
     */

    @EventHandler
    public void onFish(final PlayerFishEvent event) {
        if (event.getCaught() instanceof Player) {
            final Player player = (Player) event.getCaught();

            final PlayerData data = VulcanLite.INSTANCE.getPlayerDataManager().getPlayerData(player);

            handle: {
                if (data == null) break handle;

                data.getPositionProcessor().handleFishingRod();
            }
        }
    }
}
