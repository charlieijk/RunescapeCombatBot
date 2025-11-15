package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.wrappers.KSItem;
import rs.kreme.ksbot.api.queries.ItemQuery;

/**
 * CheckEquipmentTask - Responsible for monitoring equipment durability
 * 
 * This task handles:
 * 1. Checking equipment for low durability
 * 2. Warning when equipment needs repair
 * 3. Preventing equipment breakage
 */
public class CheckEquipmentTask extends Task {
    
    private final CombatBot bot;
    private final int DURABILITY_THRESHOLD = 10; // Percentage threshold for durability warnings
    
    public CheckEquipmentTask(CombatBot bot) {
        this.bot = bot;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. Equipment checking is enabled in configuration
        // 2. We're not in immediate danger (not eating)
        return bot.isCheckEquipment() && 
               bot.ctx.combat.getHealthPercent() > bot.getEatAtHealthPercent();
    }
    
    @Override
    public int execute() {
        // Check equipment slots for durability
        checkEquipmentDurability();
        
        // This task doesn't need to run frequently
        return 30000; // Check every 30 seconds
    }
    
    /**
     * Checks all equipment slots for low durability items
     */
    private void checkEquipmentDurability() {
        // Get all equipped items
        ItemQuery equippedItems = bot.ctx.equipment.getItems();
        
        for (KSItem item : equippedItems.results()) {
            // Check if item has durability property
            if (hasDurability(item)) {
                int durability = getDurability(item);
                
                // Warn if durability is below threshold
                if (durability < DURABILITY_THRESHOLD) {
                    System.out.println("WARNING: " + item.getName() + " has low durability (" + durability + "%)");
                }
            }
        }
    }
    
    /**
     * Checks if an item has a durability property
     * 
     * @param item The item to check
     * @return true if the item has durability, false otherwise
     */
    private boolean hasDurability(KSItem item) {
        // This is a simplified implementation
        // In a real bot, you would check item properties or IDs
        String name = item.getName().toLowerCase();
        return name.contains("degraded") || 
               name.contains("barrows") || 
               name.contains("crystal");
    }
    
    /**
     * Gets the durability percentage of an item
     * 
     * @param item The item to check
     * @return The durability percentage (0-100)
     */
    private int getDurability(KSItem item) {
        // This is a simplified implementation
        // In a real bot, you would get the actual durability from item properties
        // For demonstration purposes, we'll return a random value
        return 20 + (int)(Math.random() * 80);
    }
}
