package rs.kreme.ksbot.api.scripts.task;

import java.util.ArrayList;
import java.util.List;
import rs.kreme.ksbot.api.game.Combat;
import rs.kreme.ksbot.api.game.Consumables;
import rs.kreme.ksbot.api.game.Prayer;
import rs.kreme.ksbot.api.queries.GroundItemQuery;
import rs.kreme.ksbot.api.queries.ItemQuery;
import rs.kreme.ksbot.api.queries.ObjectQuery;
import rs.kreme.ksbot.api.wrappers.KSGroundItem;
import rs.kreme.ksbot.api.wrappers.KSItem;
import rs.kreme.ksbot.api.wrappers.KSObject;
import rs.kreme.ksbot.api.wrappers.KSPlayer;

/**
 * Simplified base class that mimics the behaviour of the KS Bot task system.
 * It provides a small in-memory context so the showcase scripts can compile
 * and run unit tests without the full client runtime.
 */
public abstract class TaskScript {

    protected final List<Task> tasks = new ArrayList<>();
    public final ScriptContext ctx = new ScriptContext();

    protected void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Lifecycle hooks provided so extending scripts can override them.
     */
    public boolean onStart() {
        return true;
    }

    public void onStop() {
        // no-op
    }

    /**
     * Tiny DTO representing all API entry points the script relies on.
     */
    public static class ScriptContext {
        public final Combat combat = new Combat();
        public final Consumables consumables = new Consumables();
        public final InventoryClient inventory = new InventoryClient();
        public final EquipmentClient equipment = new EquipmentClient();
        public final GroundItemsClient groundItems = new GroundItemsClient();
        public final PlayersClient players = new PlayersClient();
        public final ObjectsClient objects = new ObjectsClient();
        public final Prayer prayer = new Prayer();
    }

    public static class InventoryClient {
        public boolean isFull() {
            return false;
        }

        public ItemQuery getItems(String... names) {
            return new ItemQuery();
        }
    }

    public static class EquipmentClient {
        public ItemQuery getItems(String... names) {
            return new ItemQuery();
        }
    }

    public static class GroundItemsClient {
        public GroundItemQuery getItems() {
            return new GroundItemQuery();
        }
    }

    public static class PlayersClient {
        private final KSPlayer local = new KSPlayer();

        public KSPlayer getLocal() {
            return local;
        }
    }

    public static class ObjectsClient {
        public ObjectQuery getObjects(String... names) {
            return new ObjectQuery();
        }
    }
}
