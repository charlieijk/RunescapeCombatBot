package rs.kreme.ksbot.api.scripts.combatbot;

import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.scripts.task.Task;
import rs.kreme.ksbot.api.wrappers.KSObject;
import rs.kreme.ksbot.api.queries.ObjectQuery;

/**
 * SafeSpotTask - Responsible for finding and using safe spots during combat
 * 
 * This task handles:
 * 1. Finding suitable safe spots for ranged/magic combat
 * 2. Moving to safe spots when appropriate
 * 3. Ensuring the player stays in the safe spot during combat
 */
public class SafeSpotTask extends Task {
    
    private final CombatBot bot;
    private boolean inSafeSpot = false;
    
    // Coordinates of known safe spots (simplified implementation)
    private final int[][] SAFE_SPOTS = {
        {3200, 3200}, // Example coordinates
        {3210, 3205},
        {3195, 3210}
    };
    
    // Distance threshold for considering player in a safe spot
    private final int SAFE_SPOT_THRESHOLD = 3;
    
    public SafeSpotTask(CombatBot bot) {
        this.bot = bot;
    }
    
    @Override
    public boolean validate() {
        // This task should run when:
        // 1. We're using ranged or magic combat
        // 2. We're not already in a safe spot
        // 3. We're not in immediate danger (not eating)
        return isRangedOrMagicCombat() && 
               !inSafeSpot &&
               bot.ctx.combat.getHealthPercent() > bot.getEatAtHealthPercent();
    }
    
    @Override
    public int execute() {
        // Find the nearest safe spot
        int[] safeSpot = findNearestSafeSpot();
        
        if (safeSpot != null) {
            // Move to the safe spot
            System.out.println("Moving to safe spot at: " + safeSpot[0] + ", " + safeSpot[1]);
            moveToSafeSpot(safeSpot);
            inSafeSpot = true;
            
            // Wait for movement to complete
            return 3000; // Return sleep time in milliseconds
        }
        
        // No suitable safe spot found
        System.out.println("No suitable safe spot found");
        return 1000;
    }
    
    /**
     * Checks if we're using ranged or magic combat
     * 
     * @return true if using ranged or magic, false otherwise
     */
    private boolean isRangedOrMagicCombat() {
        // Get the current attack style
        Combat.AttackStyle attackStyle = bot.ctx.combat.getAttackStyle();
        
        // Check if it's ranged or magic
        return attackStyle == Combat.AttackStyle.RANGING || 
               attackStyle == Combat.AttackStyle.CASTING;
    }
    
    /**
     * Finds the nearest safe spot to the player
     * 
     * @return Coordinates of the nearest safe spot, or null if none found
     */
    private int[] findNearestSafeSpot() {
        // Get player position
        int playerX = bot.ctx.players.getLocal().getX();
        int playerY = bot.ctx.players.getLocal().getY();
        
        int[] nearestSpot = null;
        double nearestDistance = Double.MAX_VALUE;
        
        // Find the nearest safe spot
        for (int[] spot : SAFE_SPOTS) {
            double distance = calculateDistance(playerX, playerY, spot[0], spot[1]);
            
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestSpot = spot;
            }
        }
        
        return nearestSpot;
    }
    
    /**
     * Calculates the distance between two points
     * 
     * @param x1 First point x coordinate
     * @param y1 First point y coordinate
     * @param x2 Second point x coordinate
     * @param y2 Second point y coordinate
     * @return The distance between the points
     */
    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    /**
     * Moves the player to a safe spot
     * 
     * @param safeSpot Coordinates of the safe spot
     */
    private void moveToSafeSpot(int[] safeSpot) {
        // In a real implementation, you would use the API to move to the location
        // For example:
        // bot.ctx.movement.walkTo(new Tile(safeSpot[0], safeSpot[1]));
        
        // For demonstration purposes, we'll just log the action
        System.out.println("Walking to safe spot at: " + safeSpot[0] + ", " + safeSpot[1]);
    }
    
    /**
     * Checks if the player is in a safe spot
     * 
     * @return true if in a safe spot, false otherwise
     */
    public boolean checkInSafeSpot() {
        // Get player position
        int playerX = bot.ctx.players.getLocal().getX();
        int playerY = bot.ctx.players.getLocal().getY();
        
        // Check if player is near any safe spot
        for (int[] spot : SAFE_SPOTS) {
            double distance = calculateDistance(playerX, playerY, spot[0], spot[1]);
            
            if (distance <= SAFE_SPOT_THRESHOLD) {
                return true;
            }
        }
        
        // Not in a safe spot
        inSafeSpot = false;
        return false;
    }
}
