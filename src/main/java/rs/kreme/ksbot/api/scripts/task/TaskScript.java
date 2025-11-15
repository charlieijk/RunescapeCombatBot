package rs.kreme.ksbot.api.scripts.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
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

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Very small event loop that runs the task list a fixed number of times.
     */
    public void runLoop(int iterations) {
        for (int i = 0; i < iterations; i++) {
            boolean executed = false;
            for (Task task : tasks) {
                if (task.validate()) {
                    int delay = Math.max(task.execute(), 0);
                    executed = true;
                    if (delay > 0) {
                        try {
                            Thread.sleep(Math.min(delay, 2000));
                        } catch (InterruptedException ignored) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    break;
                }
            }
            if (!executed) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
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
        private final List<KSItem> items = new ArrayList<>();
        private boolean full;

        public boolean isFull() {
            return full;
        }

        public void setFull(boolean full) {
            this.full = full;
        }

        public void setItems(List<KSItem> newItems) {
            items.clear();
            items.addAll(newItems);
        }

        public ItemQuery getItems(String... names) {
            if (names == null || names.length == 0) {
                return new ItemQuery(items);
            }
            List<String> filters = Arrays.stream(names)
                    .map(name -> name.toLowerCase(Locale.ROOT))
                    .collect(Collectors.toList());
            List<KSItem> matches = items.stream()
                    .filter(item -> filters.contains(item.getName().toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
            return new ItemQuery(matches);
        }
    }

    public static class EquipmentClient {
        private final List<KSItem> items = new ArrayList<>();

        public void setItems(List<KSItem> newItems) {
            items.clear();
            items.addAll(newItems);
        }

        public ItemQuery getItems(String... names) {
            return new ItemQuery(items);
        }
    }

    public static class GroundItemsClient {
        private final List<KSGroundItem> items = new ArrayList<>();

        public void setItems(List<KSGroundItem> newItems) {
            items.clear();
            items.addAll(newItems);
        }

        public GroundItemQuery getItems() {
            return new GroundItemQuery(items);
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
