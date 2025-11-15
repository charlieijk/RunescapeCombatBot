package rs.kreme.ksbot.api.wrappers;

/**
 * Represents an item lying on the ground.
 */
public class KSGroundItem {
    private final String name;

    public KSGroundItem() {
        this("Ground item");
    }

    public KSGroundItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void interact(String action) {
        // Placeholder for world interaction.
    }
}
