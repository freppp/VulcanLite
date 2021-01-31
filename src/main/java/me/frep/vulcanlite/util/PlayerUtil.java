package me.frep.vulcanlite.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class PlayerUtil {

    /**
     * @param player - The player you want to read the effect from
     * @param effect - The potion effect you want to get the amplifier of
     * @return - The amplifier added by one to make things more readable
     */

    public int getPotionLevel(final Player player, final PotionEffectType effect) {
        if (player.hasPotionEffect(effect)) return 0;

        final int effectId = effect.getId();

        for (final PotionEffect active : player.getActivePotionEffects()) {
            if (effectId == active.getType().getId()) {
                return active.getAmplifier() + 1;
            }
        }

        return 0;
    }
}
