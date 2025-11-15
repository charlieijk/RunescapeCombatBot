package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.wrappers.KSNPC;
import rs.kreme.ksbot.api.queries.NPCQuery;

/**
 * AttackNpcTask - Responsible for finding and attacking NPCs
 * 
 * This task handles:
 * 1. Finding NPCs that match the target names
 * 2. Attacking NPCs if not already in combat
 * 3. Waiting while in combat
 * 4. Tracking kills for statistics
 */
public class AttackNpcTask extends Task {
    
    private final CombatBot bot;
    private final Combat combat;
    private KSNPC lastTarget;
    
    public AttackNpcTask(CombatBot bot) {
        this.bot = bot;
        this.combat = bot.ctx.combat;
        this.lastTarget = null;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. We're not in combat
        // 2. Our health is above the eating threshold (to prioritize eating)
        return !combat.inCombat() && 
               combat.getHealthPercent() > bot.getEatAtHealthPercent();
    }
    
    @Override
    public int execute() {
        // Check if our last target died (was non-null but now we're not in combat)
        if (lastTarget != null && !combat.isAttacking(lastTarget)) {
            System.out.println("Target defeated: " + lastTarget.getName());
            bot.incrementKillCount();
            lastTarget = null;
        }
        
        // Find an attackable NPC from our target list
        KSNPC target = findTargetNpc();
        
        if (target != null) {
            // If we found a target, attack it
            System.out.println("Attacking: " + target.getName() + " (Level: " + target.getCombatLevel() + ")");
            target.interact("Attack");
            lastTarget = target;
            
            // Wait a moment for the attack to begin
            return 1200; // Return sleep time in milliseconds
        }
        
        // No target found, wait a short time before trying again
        return 600;
    }
    
    /**
     * Finds an NPC to attack based on the configured target names
     * 
     * @return The NPC to attack, or null if none found
     */
    private KSNPC findTargetNpc() {
        // Get the list of target NPC names
        String[] targetNames = bot.getTargetNpcNames();
        
        // Try to find an attackable NPC with one of our target names
        NPCQuery query = combat.getAttackableNPC(targetNames);
        
        // Return the first NPC found, or null if none found
        return query.results().isEmpty() ? null : query.results().get(0);
    }
}
