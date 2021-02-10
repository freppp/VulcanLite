package me.frap.vulcanlite.packet;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import lombok.Getter;

/**
 * @author frap, Tecnio
 * @since 01/31/2021
 *
 * Very useful object which makes the code much cleaner - instead of checking packet IDs and having separate listeners
 * outbound and inbound packets, we can do it all in one and much cleaner.
 */

@Getter
public class Packet {

    private final Direction direction;
    private final NMSPacket rawPacket;
    private final byte packetId;

    public Packet(final Direction direction, final NMSPacket rawPacket, final byte packetId) {
        this.direction = direction;
        this.rawPacket = rawPacket;
        this.packetId = packetId;
    }

    public boolean isReceiving() {
        return direction == Direction.RECEIVE;
    }

    public boolean isSending() {
        return direction == Direction.SEND;
    }

    public boolean isFlying() {
        return isReceiving() && PacketType.Play.Client.Util.isInstanceOfFlying(packetId);
    }

    public boolean isUseEntity() {
        return isReceiving() && packetId == PacketType.Play.Client.USE_ENTITY;
    }

    public boolean isRotation() {
        return isReceiving() && (packetId == PacketType.Play.Client.LOOK || packetId == PacketType.Play.Client.POSITION_LOOK);
    }

    public boolean isPosition() {
        return isReceiving() && (packetId == PacketType.Play.Client.POSITION || packetId == PacketType.Play.Client.POSITION_LOOK);
    }

    public boolean isOpenWindow() {
        return isSending() && packetId == PacketType.Play.Server.OPEN_WINDOW;
    }

    public boolean isArmAnimation() {
        return isReceiving() && packetId == PacketType.Play.Client.ARM_ANIMATION;
    }

    public boolean isAbilities() {
        return isReceiving() && packetId == PacketType.Play.Client.ABILITIES;
    }

    public boolean isCustomPayload() {
        return isReceiving() && packetId == PacketType.Play.Client.CUSTOM_PAYLOAD;
    }

    public boolean isChat() {
        return isReceiving() && packetId == PacketType.Play.Client.CHAT;
    }

    public boolean isTabComplete() {
        return isReceiving() && packetId == PacketType.Play.Client.TAB_COMPLETE;
    }


    public boolean isBlockPlace() {
        return isReceiving() && PacketType.Play.Client.Util.isBlockPlace(packetId);
    }

    public boolean isBlockDig() {
        return isReceiving() && packetId == PacketType.Play.Client.BLOCK_DIG;
    }

    public boolean isWindowClick() {
        return isReceiving() && packetId == PacketType.Play.Client.WINDOW_CLICK; }

    public boolean isEntityAction() {
        return isReceiving() && packetId == PacketType.Play.Client.ENTITY_ACTION;
    }

    public boolean isCloseWindow() {
        return isReceiving() && packetId == PacketType.Play.Client.CLOSE_WINDOW;
    }

    public boolean isKeepAlive() {
        return isReceiving() && packetId == PacketType.Play.Client.KEEP_ALIVE;
    }

    public boolean isSteerVehicle() {
        return isReceiving() && packetId == PacketType.Play.Client.STEER_VEHICLE;
    }

    public boolean isHeldItemSlot() {
        return isReceiving() && packetId == PacketType.Play.Client.HELD_ITEM_SLOT;
    }

    public boolean isClientCommand() {
        return isReceiving() && packetId == PacketType.Play.Client.CLIENT_COMMAND;
    }

    public boolean isTransaction() {
        return isReceiving() && packetId == PacketType.Play.Client.TRANSACTION;
    }

    public boolean isTeleport() {
        return isSending() && packetId == PacketType.Play.Server.POSITION;
    }

    public boolean isVehicle() {
        return isReceiving() && (packetId == PacketType.Play.Client.VEHICLE_MOVE || packetId == PacketType.Play.Client.STEER_VEHICLE);
    }

    public boolean isVelocity() {
        return isSending() && packetId == PacketType.Play.Server.ENTITY_VELOCITY;
    }

    public boolean isPositionLook() {
        return isReceiving() && packetId == PacketType.Play.Client.POSITION_LOOK;
    }

    public enum Direction { SEND, RECEIVE }
}
