package me.frep.vulcanlite;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import lombok.Getter;
import me.frep.vulcanlite.alert.AlertManager;
import me.frep.vulcanlite.config.Config;
import me.frep.vulcanlite.data.manager.PlayerDataManager;
import me.frep.vulcanlite.listener.PlayerListener;
import me.frep.vulcanlite.network.NetworkManager;
import me.frep.vulcanlite.packet.impl.ReceivingPacketProcessor;
import me.frep.vulcanlite.packet.impl.SendingPacketProcessor;
import me.frep.vulcanlite.tick.TickManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public enum VulcanLite {

    INSTANCE;

    private VulcanLitePlugin plugin;

    private final TickManager tickManager = new TickManager();

    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();
    private final SendingPacketProcessor sendingPacketProcessor = new SendingPacketProcessor();

    private final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    private final ExecutorService packetExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService alertExecutor = Executors.newSingleThreadExecutor();

    private final AlertManager alertManager = new AlertManager();
    private final PlayerDataManager playerDataManager = new PlayerDataManager();

    public void start(final VulcanLitePlugin plugin) {
        this.plugin = plugin;

        assert plugin != null : "Error while starting Vulcan Lite!";

        pluginManager.registerEvents(new PlayerListener(), plugin);

        PacketEvents.get().registerListener(new NetworkManager(PacketEventPriority.NORMAL));

        Config.initialize();
        Config.Values.update();

        tickManager.start();
    }

    public void stop(final VulcanLitePlugin plugin) {
        this.plugin = plugin;

        assert plugin != null : "Error while shutting down Vulcan Lite!";

        tickManager.stop();
    }
}
