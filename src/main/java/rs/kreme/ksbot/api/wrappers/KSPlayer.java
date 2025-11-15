package rs.kreme.ksbot.api.wrappers;

/**
 * Minimal local player wrapper with coordinates for safe spot checks.
 */
public class KSPlayer {
    private int x = 3200;
    private int y = 3200;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
