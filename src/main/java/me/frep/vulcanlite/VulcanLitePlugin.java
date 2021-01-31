package me.frep.vulcanlite;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author frep
 * @since 01/19/2021
 *
 * You don't have to do your main class like this, however I prefer it since it's less cluttered a lot cleaner.
 */

public class VulcanLitePlugin extends JavaPlugin {

    /*
     * Create and load PacketEvents. If you have to change any settings in PacketEvents, do it here.
     */

    @Override
    public void onLoad() {
        PacketEvents.create(this);
        PacketEvents.get().load();
    }

    /*
     * Initialize PacketEvents and start the plugin.
     */

    @Override
    public void onEnable() {
        PacketEvents.get().init(this);
        VulcanLite.INSTANCE.start(this);
    }

    /*
     * Stop PacketEvents and stop the plugin.
     */

    @Override
    public void onDisable() {
        PacketEvents.get().stop();
        VulcanLite.INSTANCE.stop(this);
    }
}
