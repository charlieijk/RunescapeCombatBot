package rs.kreme.ksbot.api.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rs.kreme.ksbot.api.wrappers.KSItem;

/**
 * Represents the result of querying inventory/equipment.
 */
public class ItemQuery {
    private final List<KSItem> items;

    public ItemQuery() {
        this(Collections.emptyList());
    }

    public ItemQuery(List<KSItem> items) {
        this.items = new ArrayList<>(items);
    }

    public List<KSItem> results() {
        return Collections.unmodifiableList(items);
    }
}
