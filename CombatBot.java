package rs.kreme.ksbot.api.scripts;

import rs.kreme.ksbot.api.scripts.ScriptManifest;
import rs.kreme.ksbot.api.scripts.Script;
import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.scripts.task.TaskScript;
import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.game.Consumables;
import rs.kreme.ksbot.api.wrappers.KSNPC;
import rs.kreme.ksbot.api.queries.NPCQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * CombatBot - A simple combat bot for ReasonRSPS
 * 
 * This bot demonstrates how to create a combat script using the task-based architecture.
 * It will find and attack specified NPCs, handle health monitoring, and use special attacks.
 */
@ScriptManifest(
    name = "Simple Combat Bot",
    author = "Manus",
    category = Category.COMBAT,
    description = "A simple combat bot that attacks specified NPCs",
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
    
    @Override
    public boolean onStart() {
        // Initialize the bot and add tasks
        System.out.println("Starting Simple Combat Bot");
        
        // Add tasks in order of priority
        addTask(new EatFoodTask(this));
        addTask(new SpecialAttackTask(this));
        addTask(new AttackNpcTask(this));
        
        return true;
    }
    
    @Override
    public void onStop() {
        System.out.println("Stopping Simple Combat Bot");
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
}
