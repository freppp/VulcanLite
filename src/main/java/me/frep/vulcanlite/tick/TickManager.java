package me.frep.vulcanlite.tick;

import lombok.Getter;
import me.frep.vulcanlite.VulcanLite;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author frep
 *
 * We use this to keep track of server ticks rather than relying on system time.
 */

public class TickManager implements Runnable {

    @Getter
    private int ticks;
    private static BukkitTask task;

    public void start() {
        assert task == null : "TickManager has already been started";

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(VulcanLite.INSTANCE.getPlugin(), this, 0L, 1L);
    }

    public void stop() {
        if (task == null) return;

        task.cancel();
        task = null;
    }

    @Override
    public void run() {
        ticks++;
    }
}
