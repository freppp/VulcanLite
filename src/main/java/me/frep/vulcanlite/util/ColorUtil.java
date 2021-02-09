package me.frep.vulcanlite.util;

import lombok.experimental.UtilityClass;
import me.frep.vulcanlite.config.Config;
import org.bukkit.ChatColor;

/**
 * @author frep
 * @since 01/19/2021
 *
 */

@UtilityClass
public class ColorUtil {

    /**
     * @param string - The string you want to translate.
     * @return - The string formatted with color codes with the prefix automatically translated and replaced.
     */

    public String translate(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string.replaceAll("%prefix%", Config.Values.PREFIX));
    }
}
