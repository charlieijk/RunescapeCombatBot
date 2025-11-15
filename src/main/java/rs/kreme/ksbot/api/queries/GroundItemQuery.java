package rs.kreme.ksbot.api.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rs.kreme.ksbot.api.wrappers.KSGroundItem;

/**
 * Simple container for ground item lookups.
 */
public class GroundItemQuery {
    private final List<KSGroundItem> items;

    public GroundItemQuery() {
        this(Collections.emptyList());
    }

    public GroundItemQuery(List<KSGroundItem> items) {
        this.items = new ArrayList<>(items);
    }

    public List<KSGroundItem> results() {
        return Collections.unmodifiableList(items);
    }
}
