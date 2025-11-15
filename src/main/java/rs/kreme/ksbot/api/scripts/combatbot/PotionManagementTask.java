package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.wrappers.KSItem;
import rs.kreme.ksbot.api.queries.ItemQuery;

/**
 * PotionManagementTask - Responsible for using combat potions
 * 
 * This task handles:
 * 1. Detecting when stat boosts are needed
 * 2. Using appropriate potions for different combat styles
 * 3. Managing potion doses efficiently
 */
public class PotionManagementTask extends Task {
    
    private final CombatBot bot;
    private final Combat combat;
    
    // Potion types for different combat styles
    private final String[] STRENGTH_POTIONS = {"Super strength", "Strength potion"};
    private final String[] ATTACK_POTIONS = {"Super attack", "Attack potion"};
    private final String[] DEFENSE_POTIONS = {"Super defense", "Defense potion"};
    private final String[] RANGE_POTIONS = {"Ranging potion", "Bastion potion"};
    private final String[] MAGIC_POTIONS = {"Magic potion", "Battlemage potion"};
    private final String[] COMBAT_POTIONS = {"Super combat potion", "Combat potion"};
    
    // Minimum time between potion uses (in milliseconds)
    private final long POTION_COOLDOWN = 60000; // 1 minute
    
    // Last time potions were used
    private long lastPotionTime = 0;
    
    public PotionManagementTask(CombatBot bot) {
        this.bot = bot;
        this.combat = bot.ctx.combat;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. We're in combat
        // 2. Enough time has passed since last potion use
        // 3. We have potions in our inventory
        // 4. Our stats could use a boost
        return combat.inCombat() && 
               System.currentTimeMillis() - lastPotionTime > POTION_COOLDOWN &&
               hasPotions() &&
               needsStatBoost();
    }
    
    @Override
    public int execute() {
        // Determine which potion to use based on combat style
        String[] potionsToUse = determinePotionsToUse();
        
        // Find a potion in our inventory
        KSItem potion = findPotion(potionsToUse);
        
        if (potion != null) {
            // Use the potion
            System.out.println("Using potion: " + potion.getName());
            potion.interact("Drink");
            
            // Update the last potion time
            lastPotionTime = System.currentTimeMillis();
            
            // Wait for the drinking animation
            return 1800; // Return sleep time in milliseconds
        }
        
        // No suitable potion found
        return 1000;
    }
    
    /**
     * Checks if we have any potions in our inventory
     * 
     * @return true if potions are found, false otherwise
     */
    private boolean hasPotions() {
        // Get all possible potion types
        String[] allPotions = getAllPotionTypes();
        
        // Check if we have any of these potions
        ItemQuery query = bot.ctx.inventory.getItems(allPotions);
        return !query.results().isEmpty();
    }
    
    /**
     * Checks if our stats could use a boost
     * 
     * @return true if a stat boost would be beneficial, false otherwise
     */
    private boolean needsStatBoost() {
        // This is a simplified implementation
        // In a real bot, you would check current vs. base stats
        
        // For demonstration purposes, we'll assume we need a boost
        // if we've been in combat for a while
        return combat.inCombat();
    }
    
    /**
     * Determines which potions to use based on combat style
     * 
     * @return Array of potion names to use
     */
    private String[] determinePotionsToUse() {
        // Get the current attack style
        Combat.AttackStyle attackStyle = combat.getAttackStyle();
        
        // Choose potions based on attack style
        if (attackStyle == Combat.AttackStyle.ACCURATE || 
            attackStyle == Combat.AttackStyle.AGGRESSIVE || 
            attackStyle == Combat.AttackStyle.CONTROLLED) {
            // Melee combat - try combat potion first, then individual potions
            return COMBAT_POTIONS;
        } else if (attackStyle == Combat.AttackStyle.RANGING) {
            // Ranged combat
            return RANGE_POTIONS;
        } else if (attackStyle == Combat.AttackStyle.CASTING) {
            // Magic combat
            return MAGIC_POTIONS;
        }
        
        // Default to combat potions if style is unknown
        return COMBAT_POTIONS;
    }
    
    /**
     * Finds a potion in the inventory based on the provided potion names
     * 
     * @param potionNames The names of potions to look for
     * @return The potion item, or null if none found
     */
    private KSItem findPotion(String[] potionNames) {
        // Search inventory for items matching our potion names
        ItemQuery query = bot.ctx.inventory.getItems(potionNames);
        
        // Return the first potion found, or null if none found
        return query.results().isEmpty() ? null : query.results().get(0);
    }
    
    /**
     * Gets all possible potion types
     * 
     * @return Array of all potion names
     */
    private String[] getAllPotionTypes() {
        // Combine all potion arrays
        String[] allPotions = new String[
            STRENGTH_POTIONS.length + 
            ATTACK_POTIONS.length + 
            DEFENSE_POTIONS.length + 
            RANGE_POTIONS.length + 
            MAGIC_POTIONS.length + 
            COMBAT_POTIONS.length
        ];
        
        // Copy all potion names into the combined array
        int index = 0;
        
        for (String potion : STRENGTH_POTIONS) {
            allPotions[index++] = potion;
        }
        
        for (String potion : ATTACK_POTIONS) {
            allPotions[index++] = potion;
        }
        
        for (String potion : DEFENSE_POTIONS) {
            allPotions[index++] = potion;
        }
        
        for (String potion : RANGE_POTIONS) {
            allPotions[index++] = potion;
        }
        
        for (String potion : MAGIC_POTIONS) {
            allPotions[index++] = potion;
        }
        
        for (String potion : COMBAT_POTIONS) {
            allPotions[index++] = potion;
        }
        
        return allPotions;
    }
}
