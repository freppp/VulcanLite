package me.frep.vulcanlite.data.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import lombok.Getter;
import me.frep.vulcanlite.data.PlayerData;

@Getter
public class ActionProcessor {

    private final PlayerData data;

    private boolean sprinting, sneaking;

    public ActionProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handleEntityAction(final WrappedPacketInEntityAction wrapper) {
        switch (wrapper.getAction()) {
            case START_SPRINTING:
                sprinting = true;
                break;
            case STOP_SPRINTING:
                sprinting = false;
                break;
            case START_SNEAKING:
                sneaking = true;
                break;
            case STOP_SNEAKING:
                sneaking = false;
                break;
        }
    }

    public void handleFlying(final WrappedPacketInFlying wrapper) {
        
    }
}
