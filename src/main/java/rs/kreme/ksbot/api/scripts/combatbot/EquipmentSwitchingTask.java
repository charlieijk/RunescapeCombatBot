package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.wrappers.KSItem;
import rs.kreme.ksbot.api.queries.ItemQuery;

/**
 * EquipmentSwitchingTask - Responsible for switching equipment based on combat situations
 * 
 * This task handles:
 * 1. Switching between different weapon types based on enemy weaknesses
 * 2. Equipping special attack weapons when special attack is available
 * 3. Switching to defensive equipment when health is low
 */
public class EquipmentSwitchingTask extends Task {
    
    private final CombatBot bot;
    
    // Equipment sets (item names)
    private final String[] MELEE_EQUIPMENT = {"Dragon scimitar", "Abyssal whip", "Dragon defender"};
    private final String[] RANGED_EQUIPMENT = {"Magic shortbow", "Rune crossbow", "Black d'hide"};
    private final String[] MAGIC_EQUIPMENT = {"Staff of fire", "Mystic robe top", "Mystic robe bottom"};
    private final String[] SPECIAL_WEAPONS = {"Dragon dagger", "Granite maul", "Dragon claws"};
    private final String[] DEFENSIVE_EQUIPMENT = {"Rune platebody", "Rune platelegs", "Rune full helm"};
    
    // Current equipment set
    private EquipmentSet currentSet = EquipmentSet.MELEE;
    
    // Equipment set enum
    private enum EquipmentSet {
        MELEE,
        RANGED,
        MAGIC,
        SPECIAL,
        DEFENSIVE
    }
    
    public EquipmentSwitchingTask(CombatBot bot) {
        this.bot = bot;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. We're not in immediate danger (not eating)
        // 2. We need to switch equipment based on the situation
        return bot.ctx.combat.getHealthPercent() > bot.getEatAtHealthPercent() && 
               shouldSwitchEquipment();
    }
    
    @Override
    public int execute() {
        // Determine which equipment set to switch to
        EquipmentSet targetSet = determineTargetEquipmentSet();
        
        if (targetSet != currentSet) {
            // Switch to the target equipment set
            System.out.println("Switching equipment to: " + targetSet);
            switchEquipment(targetSet);
            currentSet = targetSet;
            
            // Wait for equipment switching
            return 1800; // Return sleep time in milliseconds
        }
        
        // No equipment switch needed
        return 1000;
    }
    
    /**
     * Determines if we should switch equipment
     * 
     * @return true if equipment switch is needed, false otherwise
     */
    private boolean shouldSwitchEquipment() {
        // Check if health is low (switch to defensive)
        if (bot.ctx.combat.getHealthPercent() < 30 && currentSet != EquipmentSet.DEFENSIVE) {
            return true;
        }
        
        // Check if special attack is ready (switch to special weapon)
        if (bot.isUseSpecialAttack() && 
            bot.ctx.combat.getSpecEnergy() >= bot.getSpecialAttackEnergyThreshold() && 
            currentSet != EquipmentSet.SPECIAL) {
            return true;
        }
        
        // Check enemy weakness (simplified implementation)
        String enemyName = getCurrentEnemyName();
        if (enemyName != null) {
            if (isWeakToMagic(enemyName) && currentSet != EquipmentSet.MAGIC) {
                return true;
            } else if (isWeakToRanged(enemyName) && currentSet != EquipmentSet.RANGED) {
                return true;
            } else if (isWeakToMelee(enemyName) && currentSet != EquipmentSet.MELEE) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Determines the target equipment set based on the current situation
     * 
     * @return The target equipment set
     */
    private EquipmentSet determineTargetEquipmentSet() {
        // Check if health is low (switch to defensive)
        if (bot.ctx.combat.getHealthPercent() < 30) {
            return EquipmentSet.DEFENSIVE;
        }
        
        // Check if special attack is ready (switch to special weapon)
        if (bot.isUseSpecialAttack() && 
            bot.ctx.combat.getSpecEnergy() >= bot.getSpecialAttackEnergyThreshold()) {
            return EquipmentSet.SPECIAL;
        }
        
        // Check enemy weakness (simplified implementation)
        String enemyName = getCurrentEnemyName();
        if (enemyName != null) {
            if (isWeakToMagic(enemyName)) {
                return EquipmentSet.MAGIC;
            } else if (isWeakToRanged(enemyName)) {
                return EquipmentSet.RANGED;
            } else if (isWeakToMelee(enemyName)) {
                return EquipmentSet.MELEE;
            }
        }
        
        // Default to current set if no switch is needed
        return currentSet;
    }
    
    /**
     * Switches to the specified equipment set
     * 
     * @param targetSet The equipment set to switch to
     */
    private void switchEquipment(EquipmentSet targetSet) {
        // Get the equipment items for the target set
        String[] equipmentItems = getEquipmentItems(targetSet);
        
        // Equip each item
        for (String itemName : equipmentItems) {
            KSItem item = findItem(itemName);
            if (item != null) {
                System.out.println("Equipping: " + item.getName());
                item.interact("Wield", "Wear", "Equip");
            }
        }
    }
    
    /**
     * Gets the equipment items for the specified set
     * 
     * @param set The equipment set
     * @return Array of item names for the set
     */
    private String[] getEquipmentItems(EquipmentSet set) {
        switch (set) {
            case MELEE:
                return MELEE_EQUIPMENT;
            case RANGED:
                return RANGED_EQUIPMENT;
            case MAGIC:
                return MAGIC_EQUIPMENT;
            case SPECIAL:
                return SPECIAL_WEAPONS;
            case DEFENSIVE:
                return DEFENSIVE_EQUIPMENT;
            default:
                return MELEE_EQUIPMENT;
        }
    }
    
    /**
     * Finds an item in the inventory by name
     * 
     * @param itemName The name of the item to find
     * @return The item, or null if not found
     */
    private KSItem findItem(String itemName) {
        // Search inventory for the item
        ItemQuery query = bot.ctx.inventory.getItems(itemName);
        
        // Return the first item found, or null if none found
        return query.results().isEmpty() ? null : query.results().get(0);
    }
    
    /**
     * Gets the name of the current enemy
     * 
     * @return The enemy name, or null if not in combat
     */
    private String getCurrentEnemyName() {
        // In a real implementation, you would use the API to get the current target
        // For example:
        // KSNPC target = bot.ctx.npcs.getNearest(npc -> npc.isInteractingWithMe());
        // return target != null ? target.getName() : null;
        
        // For demonstration purposes, we'll return a default value
        return bot.ctx.combat.inCombat() ? "Goblin" : null;
    }
    
    /**
     * Checks if an enemy is weak to magic
     * 
     * @param enemyName The name of the enemy
     * @return true if weak to magic, false otherwise
     */
    private boolean isWeakToMagic(String enemyName) {
        // This is a simplified implementation
        // In a real bot, you would use a database of enemy weaknesses
        
        String name = enemyName.toLowerCase();
        return name.contains("dragon") || 
               name.contains("demon") || 
               name.contains("tzhaar");
    }
    
    /**
     * Checks if an enemy is weak to ranged
     * 
     * @param enemyName The name of the enemy
     * @return true if weak to ranged, false otherwise
     */
    private boolean isWeakToRanged(String enemyName) {
        // This is a simplified implementation
        // In a real bot, you would use a database of enemy weaknesses
        
        String name = enemyName.toLowerCase();
        return name.contains("mage") || 
               name.contains("wizard") || 
               name.contains("aviansie");
    }
    
    /**
     * Checks if an enemy is weak to melee
     * 
     * @param enemyName The name of the enemy
     * @return true if weak to melee, false otherwise
     */
    private boolean isWeakToMelee(String enemyName) {
        // This is a simplified implementation
        // In a real bot, you would use a database of enemy weaknesses
        
        String name = enemyName.toLowerCase();
        return name.contains("ranger") || 
               name.contains("archer") || 
               name.contains("bat");
    }
}
