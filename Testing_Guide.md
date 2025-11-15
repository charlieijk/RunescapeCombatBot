# Combat Bot Testing Guide

This guide explains how to test and debug your combat bot implementation for the ReasonRSPS client.

## Prerequisites

Before testing your bot, ensure you have:

1. Compiled all Java files
2. Placed the compiled classes in the client's scripts directory
3. Set up a test account with appropriate equipment and supplies
4. Selected a suitable testing area with target NPCs

## Testing Process

### Initial Setup Testing

1. **Script Loading Test**
   - Start the client
   - Verify the bot appears in the scripts list
   - Check that the manifest information (name, author, description) is displayed correctly

2. **Configuration Test**
   - Start the bot
   - Verify that the bot initializes without errors
   - Check the console for the "Starting Advanced Combat Bot" message

### Basic Functionality Testing

3. **NPC Detection Test**
   - Run the bot in an area with target NPCs
   - Verify that the bot can detect and identify target NPCs
   - Check the console for "Attacking: [NPC Name]" messages

4. **Combat Test**
   - Verify that the bot engages in combat with NPCs
   - Check that the bot correctly tracks kills
   - Verify combat continues until interrupted

5. **Food Consumption Test**
   - Allow your character's health to drop below the configured threshold
   - Verify that the bot identifies and consumes food
   - Check the console for "Eating: [Food Name]" messages

### Advanced Feature Testing

6. **Special Attack Test**
   - Equip a weapon with a special attack
   - Verify that the bot activates special attacks when energy is sufficient
   - Check the console for "Enabling special attack" messages

7. **Prayer Test**
   - Enable prayer usage in the configuration
   - Verify that the bot activates and deactivates prayers appropriately
   - Check the console for prayer-related messages

8. **Equipment Switching Test**
   - Configure multiple equipment sets
   - Verify that the bot switches equipment based on combat situations
   - Check the console for "Switching equipment to:" messages

9. **Safe Spot Test**
   - Configure the bot for ranged or magic combat
   - Verify that the bot identifies and moves to safe spots
   - Check that the bot maintains position in the safe spot during combat

10. **Progress Tracking Test**
    - Run the bot for at least 5 minutes
    - Verify that progress reports are generated
    - Check that the statistics (kills, XP, rates) are reasonable

## Debugging Techniques

### Console Logging

Add detailed logging statements to track the bot's decision-making process:

```java
System.out.println("DEBUG: Task " + getClass().getSimpleName() + " validating...");
System.out.println("DEBUG: Health: " + combat.getHealthPercent() + "%, In combat: " + combat.inCombat());
```

### Task Validation Debugging

If a task isn't running when expected, check its validation logic:

```java
@Override
public boolean validate() {
    boolean healthCheck = combat.getHealthPercent() <= bot.getEatAtHealthPercent();
    boolean foodCheck = hasFood();
    System.out.println("DEBUG: EatFoodTask - Health check: " + healthCheck + ", Food check: " + foodCheck);
    return healthCheck && foodCheck;
}
```

### Isolating Tasks

To test a specific task in isolation, temporarily modify the `onStart()` method to only add that task:

```java
@Override
public boolean onStart() {
    System.out.println("Starting Task Isolation Test");
    // Only add the task you want to test
    addTask(new EatFoodTask(this));
    return true;
}
```

### Common Issues and Solutions

1. **Bot not attacking NPCs**
   - Check if target NPC names are correct
   - Verify that NPCs are within range
   - Ensure the bot is not stuck in another task (like eating)

2. **Bot not eating food**
   - Verify food names are correct
   - Check inventory for the specified food
   - Ensure health is below the eating threshold

3. **Special attacks not activating**
   - Check if special attack is enabled in configuration
   - Verify special attack energy is above threshold
   - Ensure the bot is in combat

4. **Equipment switching issues**
   - Verify equipment names are correct
   - Check inventory for the specified items
   - Ensure switching conditions are being met

5. **Safe spot not working**
   - Check safe spot coordinates
   - Verify the bot is using ranged or magic combat
   - Ensure pathfinding is working correctly

## Performance Testing

To evaluate your bot's performance:

1. **Efficiency Metrics**
   - Run the bot for at least 30 minutes
   - Record kills per hour, XP per hour, and resource consumption
   - Compare with manual play to assess efficiency

2. **Resource Usage**
   - Monitor food and potion consumption rates
   - Calculate cost per hour of operation
   - Evaluate profit/loss based on loot value

3. **Stability Testing**
   - Run the bot for extended periods (2+ hours)
   - Check for memory leaks or performance degradation
   - Verify the bot can recover from unexpected situations

## Adapting Tests for Different Scenarios

### Boss Fighting Tests

When testing boss-specific adaptations:
- Verify special equipment switching works for boss mechanics
- Test prayer flicking if implemented
- Check healing logic handles higher damage

### AFK Training Tests

For AFK training setups:
- Test the bot's ability to run unsupervised
- Verify anti-ban measures are effective
- Check that the bot handles unexpected interruptions

## Conclusion

Thorough testing is essential for creating reliable bots. By systematically testing each component and the integrated system, you can identify and fix issues before deploying your bot for extended use. Remember that testing is an iterative process - continue to refine your bot based on test results and real-world performance.
