package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.wrappers.KSGroundItem;
import rs.kreme.ksbot.api.queries.GroundItemQuery;

import java.util.Arrays;
import java.util.List;

/**
 * LootItemsTask - Responsible for looting items from the ground
 * 
 * This task handles:
 * 1. Finding valuable items on the ground
 * 2. Looting items based on priority
 * 3. Managing inventory space
 */
public class LootItemsTask extends Task {
    
    private final CombatBot bot;
    
    // Items to always loot regardless of value
    private final List<String> highPriorityItems = Arrays.asList(
        "Dragon bones", "Ensouled head", "Clue scroll", "Key", "Totem"
    );
    
    // Minimum value for items to loot (in gold)
    private final int MIN_VALUE_TO_LOOT = 1000;
    
    public LootItemsTask(CombatBot bot) {
        this.bot = bot;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. We're not in combat (to avoid interrupting combat)
        // 2. We have inventory space
        // 3. There are items to loot nearby
        return !bot.ctx.combat.inCombat() && 
               !bot.ctx.inventory.isFull() &&
               hasItemsToLoot();
    }
    
    @Override
    public int execute() {
        // Find the best item to loot
        KSGroundItem itemToLoot = findBestItemToLoot();
        
        if (itemToLoot != null) {
            // If we found an item, loot it
            System.out.println("Looting: " + itemToLoot.getName());
            itemToLoot.interact("Take");
            
            // Wait for the looting action
            return 1500; // Return sleep time in milliseconds
        }
        
        // No items to loot, wait a short time before trying again
        return 600;
    }
    
    /**
     * Checks if there are items worth looting nearby
     * 
     * @return true if there are items to loot, false otherwise
     */
    private boolean hasItemsToLoot() {
        // Get all ground items
        GroundItemQuery groundItems = bot.ctx.groundItems.getItems();
        
        // Check if any items meet our looting criteria
        for (KSGroundItem item : groundItems.results()) {
            if (shouldLoot(item)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Finds the best item to loot based on priority and value
     * 
     * @return The best item to loot, or null if none found
     */
    private KSGroundItem findBestItemToLoot() {
        // Get all ground items
        GroundItemQuery groundItems = bot.ctx.groundItems.getItems();
        
        KSGroundItem bestItem = null;
        int bestValue = 0;
        
        // Find the highest value item that meets our criteria
        for (KSGroundItem item : groundItems.results()) {
            if (shouldLoot(item)) {
                int itemValue = getItemValue(item);
                
                // Check if this item is better than our current best
                if (bestItem == null || itemValue > bestValue) {
                    bestItem = item;
                    bestValue = itemValue;
                }
            }
        }
        
        return bestItem;
    }
    
    /**
     * Determines if an item should be looted
     * 
     * @param item The item to check
     * @return true if the item should be looted, false otherwise
     */
    private boolean shouldLoot(KSGroundItem item) {
        // Always loot high priority items
        if (isHighPriorityItem(item)) {
            return true;
        }
        
        // Loot items above the minimum value
        return getItemValue(item) >= MIN_VALUE_TO_LOOT;
    }
    
    /**
     * Checks if an item is in the high priority list
     * 
     * @param item The item to check
     * @return true if the item is high priority, false otherwise
     */
    private boolean isHighPriorityItem(KSGroundItem item) {
        String itemName = item.getName();
        
        // Check if the item name contains any of our high priority keywords
        for (String highPriorityItem : highPriorityItems) {
            if (itemName.contains(highPriorityItem)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets the value of an item
     * 
     * @param item The item to check
     * @return The value of the item in gold
     */
    private int getItemValue(KSGroundItem item) {
        // This is a simplified implementation
        // In a real bot, you would use a price database or API
        
        // For demonstration purposes, we'll use a simple heuristic
        // based on the item name
        String name = item.getName().toLowerCase();
        
        if (name.contains("dragon")) return 20000;
        if (name.contains("rune")) return 10000;
        if (name.contains("adamant")) return 5000;
        if (name.contains("mithril")) return 2000;
        if (name.contains("steel")) return 1000;
        if (name.contains("iron")) return 500;
        if (name.contains("bronze")) return 200;
        
        // Default value for unknown items
        return 100;
    }
}
