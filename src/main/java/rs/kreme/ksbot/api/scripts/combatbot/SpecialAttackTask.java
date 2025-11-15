package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.game.Combat;

/**
 * SpecialAttackTask - Responsible for managing special attacks
 * 
 * This task handles:
 * 1. Monitoring special attack energy
 * 2. Enabling special attacks when energy is above the threshold
 * 3. Only using special attacks when in combat
 * 4. Tracking special attack usage
 */
public class SpecialAttackTask extends Task {
    
    private final CombatBot bot;
    private final Combat combat;
    private int specialAttacksUsed;
    
    public SpecialAttackTask(CombatBot bot) {
        this.bot = bot;
        this.combat = bot.ctx.combat;
        this.specialAttacksUsed = 0;
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
        specialAttacksUsed++;
        
        // Log usage statistics
        if (specialAttacksUsed % 5 == 0) {
            System.out.println("Special attack usage count: " + specialAttacksUsed);
        }
        
        // Wait a moment for the special attack to activate
        return 600; // Return sleep time in milliseconds
    }
}
