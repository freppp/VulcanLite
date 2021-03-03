package me.frap.vulcanlite;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import lombok.Getter;
import me.frap.vulcanlite.command.impl.AlertsCommand;
import me.frap.vulcanlite.config.Checks;
import me.frap.vulcanlite.config.Config;
import me.frap.vulcanlite.config.Messages;
import me.frap.vulcanlite.data.manager.PlayerDataManager;
import me.frap.vulcanlite.event.EventProcessor;
import me.frap.vulcanlite.punishment.PunishmentManager;
import me.frap.vulcanlite.tick.TickManager;
import me.frap.vulcanlite.util.CacheUtil;
import me.frap.vulcanlite.alert.AlertManager;
import me.frap.vulcanlite.check.manager.CheckManager;
import me.frap.vulcanlite.listener.PlayerListener;
import me.frap.vulcanlite.network.NetworkManager;
import me.frap.vulcanlite.packet.impl.ReceivingPacketProcessor;
import me.frap.vulcanlite.packet.impl.SendingPacketProcessor;
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

    private final PunishmentManager punishmentManager = new PunishmentManager();
    private final PlayerDataManager playerDataManager = new PlayerDataManager();
    private final AlertManager alertManager = new AlertManager();

    public void start(final VulcanLitePlugin plugin) {
        this.plugin = plugin;

        assert plugin != null : "Error while starting Vulcan Lite!";

        Config.initialize();
        Config.Values.update();

        Checks.initialize();
        Checks.Values.update();

        Messages.initialize();
        Messages.Values.update();

        getPlugin().getCommand("alerts").setExecutor(new AlertsCommand());

        CheckManager.setup();

        pluginManager.registerEvents(new PlayerListener(), plugin);
        pluginManager.registerEvents(new EventProcessor(), plugin);

        PacketEvents.get().registerListener(new NetworkManager(PacketEventPriority.NORMAL));

        tickManager.start();
    }

    public void stop(final VulcanLitePlugin plugin) {
        this.plugin = plugin;

        assert plugin != null : "Error while shutting down Vulcan Lite!";

        tickManager.stop();
    }
}
