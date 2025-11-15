package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.wrappers.KSItem;
import rs.kreme.ksbot.api.queries.ItemQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * ProgressTrackingTask - Responsible for tracking and reporting bot progress
 * 
 * This task handles:
 * 1. Tracking combat statistics (kills, XP gained, loot value)
 * 2. Monitoring resource consumption (food, potions)
 * 3. Providing periodic progress reports
 * 4. Estimating efficiency metrics (kills per hour, XP per hour)
 */
public class ProgressTrackingTask extends Task {
    
    private final CombatBot bot;
    
    // Tracking variables
    private final long startTime;
    private int startingCombatXp;
    private int currentCombatXp;
    private int foodConsumed;
    private int potionsConsumed;
    private int totalLootValue;
    private Map<String, Integer> itemsLooted;
    
    // Reporting interval (in milliseconds)
    private final long REPORT_INTERVAL = 300000; // 5 minutes
    private long lastReportTime;
    
    public ProgressTrackingTask(CombatBot bot) {
        this.bot = bot;
        this.startTime = System.currentTimeMillis();
        this.lastReportTime = startTime;
        this.startingCombatXp = getTotalCombatXp();
        this.currentCombatXp = startingCombatXp;
        this.foodConsumed = 0;
        this.potionsConsumed = 0;
        this.totalLootValue = 0;
        this.itemsLooted = new HashMap<>();
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. It's time for a progress report
        // 2. We're not in immediate danger (not eating)
        return (System.currentTimeMillis() - lastReportTime >= REPORT_INTERVAL) && 
               bot.ctx.combat.getHealthPercent() > bot.getEatAtHealthPercent();
    }
    
    @Override
    public int execute() {
        // Update tracking data
        updateTrackingData();
        
        // Generate and display progress report
        generateProgressReport();
        
        // Update last report time
        lastReportTime = System.currentTimeMillis();
        
        // This task doesn't need to run frequently
        return 1000; // Return sleep time in milliseconds
    }
    
    /**
     * Updates tracking data with current values
     */
    private void updateTrackingData() {
        // Update XP
        currentCombatXp = getTotalCombatXp();
    }
    
    /**
     * Generates and displays a progress report
     */
    private void generateProgressReport() {
        // Calculate runtime
        long runtime = System.currentTimeMillis() - startTime;
        long seconds = runtime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        // Calculate XP gained
        int xpGained = currentCombatXp - startingCombatXp;
        
        // Calculate rates
        int xpPerHour = (int)(xpGained * 3600000L / runtime);
        int killsPerHour = (int)(bot.getKillCount() * 3600000L / runtime);
        
        // Display report
        System.out.println("=== Progress Report ===");
        System.out.println("Runtime: " + hours + ":" + (minutes % 60) + ":" + (seconds % 60));
        System.out.println("Kills: " + bot.getKillCount() + " (" + killsPerHour + " per hour)");
        System.out.println("XP gained: " + xpGained + " (" + xpPerHour + " per hour)");
        System.out.println("Food consumed: " + foodConsumed);
        System.out.println("Potions consumed: " + potionsConsumed);
        System.out.println("Total loot value: " + totalLootValue + " gp");
        System.out.println("=====================");
    }
    
    /**
     * Gets the total combat XP across all combat skills
     * 
     * @return The total combat XP
     */
    private int getTotalCombatXp() {
        // In a real implementation, you would use the API to get actual XP values
        // For example:
        // return bot.ctx.skills.getXP(Skill.ATTACK) +
        //        bot.ctx.skills.getXP(Skill.STRENGTH) +
        //        bot.ctx.skills.getXP(Skill.DEFENSE) +
        //        bot.ctx.skills.getXP(Skill.HITPOINTS) +
        //        bot.ctx.skills.getXP(Skill.RANGED) +
        //        bot.ctx.skills.getXP(Skill.MAGIC);
        
        // For demonstration purposes, we'll use a simulated value
        return 1000000 + (int)(bot.getKillCount() * 100);
    }
    
    /**
     * Records food consumption
     */
    public void recordFoodConsumed() {
        foodConsumed++;
    }
    
    /**
     * Records potion consumption
     */
    public void recordPotionConsumed() {
        potionsConsumed++;
    }
    
    /**
     * Records an item being looted
     * 
     * @param itemName The name of the looted item
     * @param value The value of the looted item
     */
    public void recordItemLooted(String itemName, int value) {
        // Update total loot value
        totalLootValue += value;
        
        // Update item count
        itemsLooted.put(itemName, itemsLooted.getOrDefault(itemName, 0) + 1);
    }
}
