# Combat Bot Tutorial

## Introduction

This tutorial explains how to create a comprehensive combat bot for the ReasonRSPS client using the task-based architecture. The bot is designed to be modular, maintainable, and highly customizable.

## Project Structure

The project is organized as follows:

```
combat_bot/
└── src/
    └── main/
        └── java/
            └── rs/
                └── kreme/
                    └── ksbot/
                        └── api/
                            └── scripts/
                                └── combatbot/
                                    ├── CombatBot.java
                                    ├── AttackNpcTask.java
                                    ├── EatFoodTask.java
                                    ├── SpecialAttackTask.java
                                    ├── CheckEquipmentTask.java
                                    ├── PrayerTask.java
                                    ├── LootItemsTask.java
                                    ├── AntiBanTask.java
                                    ├── PotionManagementTask.java
                                    ├── SafeSpotTask.java
                                    ├── ProgressTrackingTask.java
                                    └── EquipmentSwitchingTask.java
```

## Core Concepts

### Task-Based Architecture

The bot uses a task-based architecture where each task is responsible for a specific aspect of the bot's functionality. Tasks are executed based on priority and validation conditions.

Key components:
- **TaskScript**: The base class that manages task execution
- **Task**: Abstract class that defines the structure of tasks
- **validate()**: Determines when a task should run
- **execute()**: Contains the task's logic and returns a sleep time

### Main Bot Class

The `CombatBot` class is the entry point for the bot. It:
- Extends `TaskScript` to use the task-based framework
- Is annotated with `@ScriptManifest` to define metadata
- Contains configuration variables for customization
- Manages the lifecycle with `onStart()` and `onStop()` methods
- Adds tasks in priority order

## Basic Tasks

### AttackNpcTask

This task handles finding and attacking NPCs:
- Finds NPCs that match the target names
- Attacks NPCs if not already in combat
- Tracks kills for statistics

```java
@Override
public boolean validate() {
    return !combat.inCombat() && 
           combat.getHealthPercent() > bot.getEatAtHealthPercent();
}

@Override
public int execute() {
    // Check if our last target died
    if (lastTarget != null && !combat.isAttacking(lastTarget)) {
        System.out.println("Target defeated: " + lastTarget.getName());
        bot.incrementKillCount();
        lastTarget = null;
    }
    
    // Find and attack a new target
    KSNPC target = findTargetNpc();
    if (target != null) {
        System.out.println("Attacking: " + target.getName());
        target.interact("Attack");
        lastTarget = target;
        return 1200;
    }
    
    return 600;
}
```

### EatFoodTask

This task handles health monitoring and food consumption:
- Monitors player health
- Finds food in the inventory
- Eats food when health is below the configured threshold
- Provides warnings when food supply is low

```java
@Override
public boolean validate() {
    return combat.getHealthPercent() <= bot.getEatAtHealthPercent() && 
           hasFood();
}

@Override
public int execute() {
    KSItem food = findFood();
    if (food != null) {
        System.out.println("Eating: " + food.getName());
        food.interact("Eat");
        return 1800;
    }
    
    System.out.println("WARNING: Health low but no food found!");
    return 600;
}
```

### SpecialAttackTask

This task manages special attacks:
- Monitors special attack energy
- Enables special attacks when energy is above the threshold
- Only uses special attacks when in combat
- Tracks special attack usage

```java
@Override
public boolean validate() {
    return bot.isUseSpecialAttack() && 
           combat.inCombat() && 
           !combat.isSpecEnabled() && 
           combat.getSpecEnergy() >= bot.getSpecialAttackEnergyThreshold();
}

@Override
public int execute() {
    System.out.println("Enabling special attack - Energy: " + combat.getSpecEnergy() + "%");
    combat.enableSpecial();
    specialAttacksUsed++;
    return 600;
}
```

## Advanced Tasks

### PotionManagementTask

This task handles combat potions:
- Detects when stat boosts are needed
- Uses appropriate potions for different combat styles
- Manages potion doses efficiently

### SafeSpotTask

This task handles safe spot usage for ranged/magic combat:
- Finds suitable safe spots
- Moves to safe spots when appropriate
- Ensures the player stays in the safe spot during combat

### ProgressTrackingTask

This task tracks and reports bot progress:
- Tracks combat statistics (kills, XP gained)
- Monitors resource consumption (food, potions)
- Provides periodic progress reports
- Estimates efficiency metrics (kills per hour, XP per hour)

### EquipmentSwitchingTask

This task handles equipment switching:
- Switches between different weapon types based on enemy weaknesses
- Equips special attack weapons when special attack is available
- Switches to defensive equipment when health is low

## Customization Guide

### Configuring Target NPCs

To change which NPCs the bot attacks, modify the `targetNpcNames` array in the `CombatBot` class:

```java
private String[] targetNpcNames = {"Goblin", "Cow", "Chicken"};
```

### Adjusting Health Threshold

To change when the bot eats food, modify the `eatAtHealthPercent` variable:

```java
private int eatAtHealthPercent = 50; // Eat when health is below 50%
```

### Enabling/Disabling Features

The bot has several features that can be enabled or disabled:

```java
private boolean useSpecialAttack = true;
private boolean usePrayer = false;
private boolean checkEquipment = true;
private boolean useAntiBan = true;
```

### Adding New Tasks

To add a new task to the bot, create a new class that extends `Task` and implement the `validate()` and `execute()` methods. Then add the task in the `onStart()` method:

```java
addTask(new YourNewTask(this));
```

## Testing and Debugging

### Running the Bot

To run the bot:
1. Compile the code
2. Place the compiled classes in the client's scripts directory
3. Start the client
4. Select "Advanced Combat Bot" from the scripts list

### Debugging Tips

- Use `System.out.println()` statements to track what the bot is doing
- Check the validate() method if a task isn't running when expected
- Adjust task priorities by changing the order in which tasks are added

### Common Issues

- **Bot not attacking**: Check if the target NPC names are correct
- **Bot not eating**: Verify that you have the correct food names configured
- **Special attacks not working**: Ensure special attack is enabled in the configuration

## Adapting for Different Scenarios

### Boss Fighting

To adapt the bot for boss fighting:
1. Change the target NPC names to the boss name
2. Adjust the health threshold for eating
3. Enable prayer and configure appropriate prayers
4. Add specific equipment for the boss

### Training Specific Skills

To focus on training specific skills:
1. Modify the attack style in the combat settings
2. Select NPCs that are appropriate for the skill
3. Configure equipment that boosts the relevant skill

### AFK Training

For more AFK-friendly training:
1. Increase the health threshold for eating
2. Select NPCs with lower combat levels
3. Enable anti-ban measures
4. Configure safe spots if using ranged or magic

## Conclusion

This combat bot demonstrates how to create a comprehensive, modular bot using the task-based architecture. By understanding the core concepts and customization options, you can adapt this bot for various combat scenarios in ReasonRSPS.
