package me.frap.vulcanlite.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;

@UtilityClass
public class BlockUtil {

    public boolean isAir(final Material material) {
        return material.isAir();
    }
}
