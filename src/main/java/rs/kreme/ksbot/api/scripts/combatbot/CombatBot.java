package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.ScriptManifest;
import rs.kreme.ksbot.api.scripts.Category;
import rs.kreme.ksbot.api.scripts.task.TaskScript;
import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.game.Consumables;
import rs.kreme.ksbot.api.wrappers.KSNPC;
import rs.kreme.ksbot.api.queries.NPCQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * CombatBot - A comprehensive combat bot for ReasonRSPS
 * 
 * This bot demonstrates how to create a combat script using the task-based architecture.
 * It will find and attack specified NPCs, handle health monitoring, use special attacks,
 * manage prayer, and include anti-ban measures.
 */
@ScriptManifest(
    name = "Advanced Combat Bot",
    author = "Manus",
    category = Category.COMBAT,
    description = "A comprehensive combat bot with multiple features",
    servers = {"ReasonRSPS"},
    version = 1.0
)
public class CombatBot extends TaskScript {
    
    // Configuration variables - modify these to customize the bot
    private String[] targetNpcNames = {"Goblin", "Cow", "Chicken"}; // NPCs to attack
    private int eatAtHealthPercent = 50; // Eat food when health is below this percentage
    private boolean useSpecialAttack = true; // Whether to use special attacks
    private int specialAttackEnergyThreshold = 50; // Use special attack when energy is above this percentage
    private String[] foodNames = {"Lobster", "Swordfish", "Shark"}; // Food items to eat
    private boolean usePrayer = false; // Whether to use prayer
    private String[] prayerNames = {"Protect from Melee"}; // Prayers to activate
    private boolean checkEquipment = true; // Whether to check equipment durability
    private boolean useAntiBan = true; // Whether to use anti-ban measures
    
    // Runtime variables
    private long startTime;
    private int killCount;
    
    @Override
    public boolean onStart() {
        // Initialize the bot and add tasks
        System.out.println("Starting Advanced Combat Bot");
        startTime = System.currentTimeMillis();
        killCount = 0;
        
        // Add tasks in order of priority (highest priority first)
        addTask(new EatFoodTask(this));
        addTask(new CheckEquipmentTask(this));
        addTask(new PrayerTask(this));
        addTask(new SpecialAttackTask(this));
        addTask(new LootItemsTask(this));
        addTask(new AttackNpcTask(this));
        addTask(new AntiBanTask(this));
        
        return true;
    }
    
    @Override
    public void onStop() {
        // Calculate runtime
        long runtime = System.currentTimeMillis() - startTime;
        long seconds = runtime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        // Print summary
        System.out.println("=== Advanced Combat Bot Summary ===");
        System.out.println("Runtime: " + hours + ":" + (minutes % 60) + ":" + (seconds % 60));
        System.out.println("NPCs killed: " + killCount);
        System.out.println("Kills per hour: " + (killCount * 3600000L / runtime));
        System.out.println("================================");
    }
    
    /**
     * Increments the kill count when an NPC is killed
     */
    public void incrementKillCount() {
        killCount++;
    }
    
    // Getter methods for configuration variables
    public String[] getTargetNpcNames() {
        return targetNpcNames;
    }
    
    public int getEatAtHealthPercent() {
        return eatAtHealthPercent;
    }
    
    public boolean isUseSpecialAttack() {
        return useSpecialAttack;
    }
    
    public int getSpecialAttackEnergyThreshold() {
        return specialAttackEnergyThreshold;
    }
    
    public String[] getFoodNames() {
        return foodNames;
    }
    
    public boolean isUsePrayer() {
        return usePrayer;
    }
    
    public String[] getPrayerNames() {
        return prayerNames;
    }
    
    public boolean isCheckEquipment() {
        return checkEquipment;
    }
    
    public boolean isUseAntiBan() {
        return useAntiBan;
    }
}
