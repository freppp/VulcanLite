package me.frep.vulcanlite.tick;

import lombok.Getter;
import me.frep.vulcanlite.VulcanLite;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author frep
 * @since 2/8/21
 *
 * We use this to keep track of server ticks rather than relying on system time.
 * We can also use this to track periods in which the server 'froze' or had a lag spike.
 */

@Getter
public class TickManager implements Runnable {

    private int ticks;
    private static BukkitTask task;
    private long lastTick, tickDelay, lastLagSpike;

    /*
     * 
     */

    public void start() {
        assert task == null : "TickManager has already been started";

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(VulcanLite.INSTANCE.getPlugin(), this, 0L, 1L);
    }

    /*
     * Stopping the task.
     */

    public void stop() {
        if (task == null) return;

        task.cancel();
        task = null;
    }

    @Override
    public void run() {
        ticks++;

        tickDelay = System.currentTimeMillis() - lastTick;
        if (tickDelay < 10) lastLagSpike = System.currentTimeMillis();
        lastTick = System.currentTimeMillis();
    }
}
