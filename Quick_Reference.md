# Bot Development Quick Reference

## Key API Classes

| Class | Purpose | Important Methods |
|-------|---------|-------------------|
| `Script` | Base class for all scripts | `onStart()`, `onStop()`, `onProcess()` |
| `TaskScript` | Script that uses task-based architecture | `addTask()` |
| `Task` | Individual bot task | `validate()`, `execute()` |
| `Combat` | Combat-related functionality | `inCombat()`, `getHealthPercent()`, `enableSpecial()` |
| `Inventory` | Inventory management | `getItems()`, `getCount()`, `getItem()` |
| `Prayer` | Prayer management | `activate()`, `deactivate()`, `isActive()` |
| `Equipment` | Equipment management | `equip()`, `unequip()`, `isEquipped()` |
| `KSNPC` | NPC interaction | `interact()`, `getName()`, `getDistance()` |
| `KSItem` | Item interaction | `interact()`, `getName()`, `getID()` |

## Common Code Snippets

### Finding and Attacking NPCs
```java
// Find NPC by name
KSNPC target = ctx.npcs.getNearest(npc -> {
    String name = npc.getName();
    return name != null && Arrays.asList(targetNames).contains(name);
});

// Attack NPC
if (target != null) {
    target.interact("Attack");
}
```

### Eating Food
```java
// Find food in inventory
KSItem food = ctx.inventory.getItem(item -> 
    Arrays.asList(foodNames).contains(item.getName()));

// Eat food
if (food != null) {
    food.interact("Eat");
}
```

### Using Special Attack
```java
// Check special attack energy
if (ctx.combat.getSpecEnergy() >= 50 && !ctx.combat.isSpecEnabled()) {
    // Enable special attack
    ctx.combat.enableSpecial();
}
```

### Activating Prayers
```java
// Activate prayer
if (!ctx.prayer.isActive("Protect from Melee")) {
    ctx.prayer.activate("Protect from Melee");
}

// Deactivate prayer
if (ctx.prayer.isActive("Protect from Melee")) {
    ctx.prayer.deactivate("Protect from Melee");
}
```

### Equipment Management
```java
// Equip item from inventory
KSItem weapon = ctx.inventory.getItem("Dragon scimitar");
if (weapon != null) {
    weapon.interact("Wield");
}

// Check if item is equipped
boolean hasWeapon = ctx.equipment.isEquipped("Dragon scimitar");
```

### Task Template
```java
public class MyTask extends Task {
    private final CombatBot bot;
    
    public MyTask(CombatBot bot) {
        this.bot = bot;
    }
    
    @Override
    public boolean validate() {
        // Return true when this task should run
        return true;
    }
    
    @Override
    public int execute() {
        // Implement task logic
        System.out.println("Executing my task");
        
        // Return sleep time in milliseconds
        return 1000;
    }
}
```

## Task Priority Order

For combat bots, the recommended task priority order is:

1. **EatFoodTask** - Highest priority to ensure survival
2. **PrayerTask** - Manage prayers before combat
3. **PotionManagementTask** - Use potions before engaging
4. **CheckEquipmentTask** - Ensure proper equipment
5. **SafeSpotTask** - Get to safe spot if using ranged/magic
6. **SpecialAttackTask** - Use special attacks during combat
7. **AttackNpcTask** - Find and attack targets
8. **LootItemsTask** - Collect drops after combat
9. **ProgressTrackingTask** - Track statistics
10. **AntiBanTask** - Lowest priority, run when nothing else to do

## Common Issues and Solutions

| Issue | Possible Cause | Solution |
|-------|----------------|----------|
| Bot not attacking | Wrong NPC names | Check target NPC names in configuration |
| | NPC not in range | Move closer to NPCs or check area |
| Bot not eating | Wrong food names | Verify food names in configuration |
| | No food in inventory | Check inventory for food items |
| Special attack not working | Not enough energy | Check special attack energy threshold |
| | Wrong weapon equipped | Verify weapon has special attack |
| Bot getting stuck | Task validation issue | Check task validation conditions |
| | Path finding problem | Implement better movement logic |

## Testing Checklist

- [ ] Script loads in client
- [ ] Bot initializes without errors
- [ ] Bot detects and attacks target NPCs
- [ ] Bot eats food when health is low
- [ ] Bot uses special attacks when configured
- [ ] Bot activates prayers when configured
- [ ] Bot switches equipment when needed
- [ ] Bot collects loot after combat
- [ ] Bot handles unexpected situations
- [ ] Bot tracks progress and statistics

## Performance Optimization Tips

1. **Reduce Object Creation**
   - Reuse objects instead of creating new ones
   - Use primitive types when possible

2. **Optimize Task Validation**
   - Keep validation methods simple and fast
   - Cache results when appropriate

3. **Manage Sleep Times**
   - Use appropriate sleep times for different actions
   - Longer sleeps for less critical tasks

4. **Limit API Calls**
   - Don't call the same API method repeatedly
   - Cache results when they won't change frequently

5. **Use Early Returns**
   - Exit methods early when conditions aren't met
   - Avoid unnecessary processing
