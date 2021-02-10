package me.frap.vulcanlite.data.impl;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import lombok.Getter;
import me.frap.vulcanlite.data.PlayerData;

@Getter
public class ActionProcessor {

    private final PlayerData data;

    private boolean sprinting, sneaking, sendingAction, placing;

    public ActionProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handleEntityAction(final WrappedPacketInEntityAction wrapper) {
        sendingAction = true;
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

    public void handleBlockPlace() {
        placing = true;
    }

    public void handleFlying() {
        sendingAction = false;
        placing = false;
    }
}
