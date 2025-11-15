package rs.kreme.ksbot.api.scripts;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.game.Consumables;
import rs.kreme.ksbot.api.wrappers.KSItem;
import rs.kreme.ksbot.api.queries.ItemQuery;

/**
 * EatFoodTask - Responsible for monitoring health and eating food when necessary
 * 
 * This task handles:
 * 1. Monitoring player health
 * 2. Finding food in the inventory
 * 3. Eating food when health is below the configured threshold
 */
public class EatFoodTask extends Task {
    
    private final CombatBot bot;
    private final Combat combat;
    private final Consumables consumables;
    
    public EatFoodTask(CombatBot bot) {
        this.bot = bot;
        this.combat = bot.ctx.combat;
        this.consumables = bot.ctx.consumables;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. Our health is below the eating threshold
        // 2. We have food in our inventory
        return combat.getHealthPercent() <= bot.getEatAtHealthPercent() && 
               hasFood();
    }
    
    @Override
    public int execute() {
        // Find food in our inventory
        KSItem food = findFood();
        
        if (food != null) {
            // If we found food, eat it
            System.out.println("Eating: " + food.getName() + " - Current health: " + 
                              combat.getCurrentHealth() + " (" + combat.getHealthPercent() + "%)");
            
            // Interact with the food item to eat it
            food.interact("Eat");
            
            // Wait a moment for the eating animation
            return 1800; // Return sleep time in milliseconds
        }
        
        // No food found, but we need to eat - this is a problem!
        System.out.println("WARNING: Health low but no food found!");
        return 600;
    }
    
    /**
     * Checks if we have any food in our inventory
     * 
     * @return true if food is found, false otherwise
     */
    private boolean hasFood() {
        return findFood() != null;
    }
    
    /**
     * Finds food in the inventory based on the configured food names
     * 
     * @return The food item, or null if none found
     */
    private KSItem findFood() {
        // Get the list of food names
        String[] foodNames = bot.getFoodNames();
        
        // Search inventory for items matching our food names
        ItemQuery query = bot.ctx.inventory.getItems(foodNames);
        
        // Return the first food item found, or null if none found
        return query.results().isEmpty() ? null : query.results().get(0);
    }
}
