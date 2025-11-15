# Combat Bot Customization Guide

This guide explains how to customize the combat bot for different scenarios and requirements.

## Configuration Variables

The bot includes several configuration variables that can be modified to change its behavior without editing the code:

```java
// Target NPCs to attack
private String[] targetNpcNames = {"Goblin", "Cow", "Chicken"};

// Health percentage threshold for eating food
private int eatAtHealthPercent = 50;

// Special attack settings
private boolean useSpecialAttack = true;
private int specialAttackEnergyThreshold = 50;

// Food items to eat
private String[] foodNames = {"Lobster", "Swordfish", "Shark"};

// Prayer settings
private boolean usePrayer = false;
private String[] prayerNames = {"Protect from Melee"};

// Equipment checking
private boolean checkEquipment = true;

// Anti-ban measures
private boolean useAntiBan = true;
```

## Scenario-Based Customization

### 1. Low-Level Training

For training at low levels (1-40):

```java
// Target weak NPCs
private String[] targetNpcNames = {"Goblin", "Cow", "Chicken", "Rat"};

// Eat sooner to avoid death
private int eatAtHealthPercent = 70;

// Disable special attack until you have appropriate weapons
private boolean useSpecialAttack = false;

// Use low-level food
private String[] foodNames = {"Shrimp", "Bread", "Cake"};

// Disable prayer to save points
private boolean usePrayer = false;
```

### 2. Mid-Level Training (40-70)

For efficient training at mid levels:

```java
// Target medium-level NPCs
private String[] targetNpcNames = {"Rock Crab", "Sand Crab", "Ammonite Crab"};

// Balance between safety and efficiency
private int eatAtHealthPercent = 50;

// Enable special attack if you have appropriate weapons
private boolean useSpecialAttack = true;
private int specialAttackEnergyThreshold = 75; // Use special more conservatively

// Use mid-level food
private String[] foodNames = {"Lobster", "Swordfish", "Monkfish"};

// Enable prayer for protection if needed
private boolean usePrayer = true;
private String[] prayerNames = {"Protect from Melee"};
```

### 3. High-Level Training (70+)

For maximizing XP at high levels:

```java
// Target high-level NPCs
private String[] targetNpcNames = {"Bandits", "Nightmare Zone", "Slayer Monster"};

// Eat less frequently to maximize DPS
private int eatAtHealthPercent = 30;

// Use special attacks aggressively
private boolean useSpecialAttack = true;
private int specialAttackEnergyThreshold = 25;

// Use high-level food
private String[] foodNames = {"Shark", "Manta ray", "Anglerfish"};

// Use offensive prayers
private boolean usePrayer = true;
private String[] prayerNames = {"Piety", "Rigour", "Augury"};
```

### 4. Boss Fighting

For fighting specific bosses:

```java
// Target specific boss
private String[] targetNpcNames = {"King Black Dragon"};

// Prioritize safety
private int eatAtHealthPercent = 70;

// Use special attacks strategically
private boolean useSpecialAttack = true;
private int specialAttackEnergyThreshold = 100; // Only use when full

// Use best food
private String[] foodNames = {"Shark", "Manta ray", "Anglerfish"};

// Use appropriate protection prayers
private boolean usePrayer = true;
private String[] prayerNames = {"Protect from Magic", "Protect from Missiles"};
```

### 5. AFK Training

For minimal interaction training:

```java
// Target NPCs that attack slowly
private String[] targetNpcNames = {"Sand Crab", "Ammonite Crab", "Nightmare Zone"};

// Eat early to avoid death while AFK
private int eatAtHealthPercent = 60;

// Disable special attack to avoid running out of energy
private boolean useSpecialAttack = false;

// Use food that heals more per inventory slot
private String[] foodNames = {"Shark", "Manta ray", "Anglerfish"};

// Enable prayer for protection
private boolean usePrayer = true;
private String[] prayerNames = {"Protect from Melee"};

// Increase anti-ban measures
private boolean useAntiBan = true;
```

## Advanced Customization

### Adding Custom Tasks

To add a custom task, create a new class that extends `Task`:

```java
public class CustomTask extends Task {
    
    private final CombatBot bot;
    
    public CustomTask(CombatBot bot) {
        this.bot = bot;
    }
    
    @Override
    public boolean validate() {
        // Define when this task should run
        return true; // Replace with your condition
    }
    
    @Override
    public int execute() {
        // Implement your task logic
        System.out.println("Executing custom task");
        
        // Return sleep time in milliseconds
        return 1000;
    }
}
```

Then add your task in the `onStart()` method:

```java
@Override
public boolean onStart() {
    System.out.println("Starting Advanced Combat Bot");
    startTime = System.currentTimeMillis();
    killCount = 0;
    
    // Add your custom task
    addTask(new CustomTask(this));
    
    // Add other tasks
    addTask(new EatFoodTask(this));
    // ...
    
    return true;
}
```

### Modifying Existing Tasks

To modify an existing task, you can extend it and override specific methods:

```java
public class EnhancedEatFoodTask extends EatFoodTask {
    
    public EnhancedEatFoodTask(CombatBot bot) {
        super(bot);
    }
    
    @Override
    public boolean validate() {
        // Add additional validation logic
        boolean baseValidation = super.validate();
        boolean enhancedCondition = /* your condition */;
        return baseValidation && enhancedCondition;
    }
    
    @Override
    public int execute() {
        // Add pre-execution logic
        System.out.println("Enhanced eating logic");
        
        // Call the original execution
        int sleepTime = super.execute();
        
        // Add post-execution logic
        
        return sleepTime;
    }
}
```

Then replace the original task with your enhanced version:

```java
// Replace EatFoodTask with EnhancedEatFoodTask
addTask(new EnhancedEatFoodTask(this));
```

### Creating Task Presets

You can create methods that add predefined sets of tasks for different scenarios:

```java
private void addMeleeCombatTasks() {
    addTask(new EatFoodTask(this));
    addTask(new SpecialAttackTask(this));
    addTask(new AttackNpcTask(this));
    // Add other melee-specific tasks
}

private void addRangedCombatTasks() {
    addTask(new EatFoodTask(this));
    addTask(new SafeSpotTask(this));
    addTask(new AttackNpcTask(this));
    // Add other ranged-specific tasks
}

@Override
public boolean onStart() {
    System.out.println("Starting Advanced Combat Bot");
    startTime = System.currentTimeMillis();
    killCount = 0;
    
    // Choose task preset based on combat style
    if (isMeleeCombat()) {
        addMeleeCombatTasks();
    } else if (isRangedCombat()) {
        addRangedCombatTasks();
    } else {
        addMagicCombatTasks();
    }
    
    return true;
}
```

## Saving and Loading Configurations

To make customization easier, you can implement configuration saving and loading:

```java
public void saveConfig(String fileName) {
    try {
        Properties props = new Properties();
        
        // Save configuration variables
        props.setProperty("targetNpcNames", String.join(",", targetNpcNames));
        props.setProperty("eatAtHealthPercent", String.valueOf(eatAtHealthPercent));
        props.setProperty("useSpecialAttack", String.valueOf(useSpecialAttack));
        // Save other properties...
        
        // Write to file
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            props.store(out, "Combat Bot Configuration");
        }
        
        System.out.println("Configuration saved to " + fileName);
    } catch (Exception e) {
        System.err.println("Error saving configuration: " + e.getMessage());
    }
}

public void loadConfig(String fileName) {
    try {
        Properties props = new Properties();
        
        // Load from file
        try (FileInputStream in = new FileInputStream(fileName)) {
            props.load(in);
        }
        
        // Load configuration variables
        String targetNpcs = props.getProperty("targetNpcNames");
        if (targetNpcs != null) {
            targetNpcNames = targetNpcs.split(",");
        }
        
        String healthPercent = props.getProperty("eatAtHealthPercent");
        if (healthPercent != null) {
            eatAtHealthPercent = Integer.parseInt(healthPercent);
        }
        
        // Load other properties...
        
        System.out.println("Configuration loaded from " + fileName);
    } catch (Exception e) {
        System.err.println("Error loading configuration: " + e.getMessage());
    }
}
```

## Conclusion

By customizing the combat bot for specific scenarios and requirements, you can maximize its effectiveness and efficiency. Whether you're training at low levels, fighting bosses, or looking for AFK training methods, the bot's modular architecture allows for flexible adaptation to your needs.

Remember to test your customizations thoroughly to ensure they work as expected. Small changes to configuration variables can have significant impacts on the bot's behavior and performance.
