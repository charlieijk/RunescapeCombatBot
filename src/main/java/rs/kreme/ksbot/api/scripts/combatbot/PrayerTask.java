package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.game.Prayer;
import rs.kreme.ksbot.api.game.Prayer.Prayers;

/**
 * PrayerTask - Responsible for managing prayer usage
 * 
 * This task handles:
 * 1. Activating configured prayers when in combat
 * 2. Deactivating prayers when not in combat to conserve prayer points
 * 3. Monitoring prayer points
 */
public class PrayerTask extends Task {
    
    private final CombatBot bot;
    private final Prayer prayer;
    
    public PrayerTask(CombatBot bot) {
        this.bot = bot;
        this.prayer = bot.ctx.prayer;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. Prayer usage is enabled in configuration
        // 2. We have prayer points available
        return bot.isUsePrayer() && 
               prayer.getPrayerPoints() > 0;
    }
    
    @Override
    public int execute() {
        // Check if we're in combat
        boolean inCombat = bot.ctx.combat.inCombat();
        
        // Get configured prayers
        String[] prayerNames = bot.getPrayerNames();
        
        // Manage prayers based on combat state
        if (inCombat) {
            // Activate prayers when in combat
            activatePrayers(prayerNames);
        } else {
            // Deactivate prayers when not in combat to save points
            deactivatePrayers(prayerNames);
        }
        
        // Log prayer points status if running low
        int prayerPoints = prayer.getPrayerPoints();
        if (prayerPoints < 20) {
            System.out.println("WARNING: Prayer points low: " + prayerPoints);
        }
        
        // Check prayer status periodically
        return 5000; // Check every 5 seconds
    }
    
    /**
     * Activates the specified prayers if they're not already active
     * 
     * @param prayerNames The names of prayers to activate
     */
    private void activatePrayers(String[] prayerNames) {
        for (String prayerName : prayerNames) {
            // Convert name to prayer enum (simplified implementation)
            Prayers prayerEnum = getPrayerByName(prayerName);
            
            if (prayerEnum != null && !prayer.isPrayerActive(prayerEnum)) {
                System.out.println("Activating prayer: " + prayerName);
                prayer.activatePrayer(prayerEnum);
            }
        }
    }
    
    /**
     * Deactivates the specified prayers if they're active
     * 
     * @param prayerNames The names of prayers to deactivate
     */
    private void deactivatePrayers(String[] prayerNames) {
        for (String prayerName : prayerNames) {
            // Convert name to prayer enum (simplified implementation)
            Prayers prayerEnum = getPrayerByName(prayerName);
            
            if (prayerEnum != null && prayer.isPrayerActive(prayerEnum)) {
                System.out.println("Deactivating prayer: " + prayerName);
                prayer.deactivatePrayer(prayerEnum);
            }
        }
    }
    
    /**
     * Converts a prayer name to its corresponding enum value
     * 
     * @param prayerName The name of the prayer
     * @return The corresponding Prayers enum value, or null if not found
     */
    private Prayers getPrayerByName(String prayerName) {
        // This is a simplified implementation
        // In a real bot, you would match the name to the actual enum values
        
        // Example mapping for common prayers
        if (prayerName.equalsIgnoreCase("Protect from Melee")) {
            return Prayers.PROTECT_FROM_MELEE;
        } else if (prayerName.equalsIgnoreCase("Protect from Magic")) {
            return Prayers.PROTECT_FROM_MAGIC;
        } else if (prayerName.equalsIgnoreCase("Protect from Missiles")) {
            return Prayers.PROTECT_FROM_MISSILES;
        } else if (prayerName.equalsIgnoreCase("Piety")) {
            return Prayers.PIETY;
        }
        
        // Default case if prayer name not recognized
        System.out.println("WARNING: Unrecognized prayer name: " + prayerName);
        return null;
    }
}
