package rs.kreme.ksbot.api.wrappers;

/**
 * Simplified world object wrapper.
 */
public class KSObject {
    private final String name;

    public KSObject() {
        this("Object");
    }

    public KSObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void interact(String action) {
        // Placeholder
    }
}
