package rs.kreme.ksbot.api.game;

import java.util.EnumSet;
import java.util.Set;

/**
 * Simplified representation of the prayer API.
 */
public class Prayer {
    private int prayerPoints = 99;
    private final Set<Prayers> activePrayers = EnumSet.noneOf(Prayers.class);

    public int getPrayerPoints() {
        return prayerPoints;
    }

    public void setPrayerPoints(int prayerPoints) {
        this.prayerPoints = prayerPoints;
    }

    public boolean isPrayerActive(Prayers prayer) {
        return activePrayers.contains(prayer);
    }

    public void activatePrayer(Prayers prayer) {
        activePrayers.add(prayer);
    }

    public void deactivatePrayer(Prayers prayer) {
        activePrayers.remove(prayer);
    }

    public enum Prayers {
        PROTECT_FROM_MELEE,
        PROTECT_FROM_MAGIC,
        PROTECT_FROM_MISSILES,
        PIETY
    }
}
