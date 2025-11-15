package rs.kreme.ksbot.api.wrappers;

/**
 * Minimal inventory/equipment item representation.
 */
public class KSItem {
    private final String name;

    public KSItem() {
        this("Item");
    }

    public KSItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void interact(String... actions) {
        // Placeholder for UI interaction.
    }
}
