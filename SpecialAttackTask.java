package rs.kreme.ksbot.api.scripts;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.game.Combat;

/**
 * SpecialAttackTask - Responsible for managing special attacks
 * 
 * This task handles:
 * 1. Monitoring special attack energy
 * 2. Enabling special attacks when energy is above the threshold
 * 3. Only using special attacks when in combat
 */
public class SpecialAttackTask extends Task {
    
    private final CombatBot bot;
    private final Combat combat;
    
    public SpecialAttackTask(CombatBot bot) {
        this.bot = bot;
        this.combat = bot.ctx.combat;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. Special attacks are enabled in the configuration
        // 2. We're in combat
        // 3. Special attack is not already enabled
        // 4. We have enough special attack energy
        return bot.isUseSpecialAttack() && 
               combat.inCombat() && 
               !combat.isSpecEnabled() && 
               combat.getSpecEnergy() >= bot.getSpecialAttackEnergyThreshold();
    }
    
    @Override
    public int execute() {
        // Enable special attack
        System.out.println("Enabling special attack - Energy: " + combat.getSpecEnergy() + "%");
        combat.enableSpecial();
        
        // Wait a moment for the special attack to activate
        return 600; // Return sleep time in milliseconds
    }
}
