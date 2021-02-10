package me.frap.vulcanlite.util;

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

    /**
     * @param player - The player you want to read the effect from
     * @param base - The base speed you want to account for.
     * @return - The 'base' parameter adjusted to account for Speed potions and walk speed.
     */

    public double getBaseSpeed(final Player player, final double base) {
        return base + getPotionLevel(player, PotionEffectType.SPEED) * 0.062f + (Math.abs(player.getWalkSpeed() - .2F) * 3.5);
    }

    /**
     * @param player - The player you want to get the max speed from.
     * @return - The base max ground speed (.288) adjusted accounting for speed potions and walk speed.
     */

    public double getBaseGroundSpeed(final Player player) {
        return getBaseSpeed(player, .288);
    }

    /**
     * @param player - The player you want to get the max speed from.
     * @return - The base max air speed ~(.36) adjusted accounting for speed potions and walk speed.
     */

    public double getBaseAirSpeed(final Player player) {
        return getBaseSpeed(player, .36);
    }
}
