package rs.kreme.ksbot.api.wrappers;

/**
 * Minimal NPC wrapper used for compilation/testing.
 */
public class KSNPC {
    private final String name;
    private final int combatLevel;

    public KSNPC() {
        this("NPC", 1);
    }

    public KSNPC(String name, int combatLevel) {
        this.name = name;
        this.combatLevel = combatLevel;
    }

    public String getName() {
        return name;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public void interact(String action) {
        // No-op placeholder
    }
}
