package me.frep.vulcanlite.data;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerData {

    private final Player player;

    public PlayerData(final Player player) {
        this.player = player;
    }
}
