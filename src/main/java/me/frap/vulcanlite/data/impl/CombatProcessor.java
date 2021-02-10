package me.frap.vulcanlite.data.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import lombok.Getter;
import me.frap.vulcanlite.data.PlayerData;
import org.bukkit.entity.Entity;

@Getter
public class CombatProcessor {

    private final PlayerData data;

    private Entity target, lastTarget;

    private int hitTicks;

    public CombatProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handleUseEntity(final WrappedPacketInUseEntity wrapper) {

        /*
         * We only care about attack packets here. If it isn't an attack packet, we just return.
         */

        if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) return;

        /*
         * Parse our target and lastTarget variables.
         */

        lastTarget = target == null ? wrapper.getEntity() : target;
        target = wrapper.getEntity();

        /*
         * Reset the hitTicks variable so that we can track when the player was recently attacking.
         */

        hitTicks = 0;
    }

    public void handleFlying() {

        /*
         * Every flying packet, we increment this variable so that we can keep track of
         * when the player was attacking.
         */

        ++hitTicks;
    }
}
