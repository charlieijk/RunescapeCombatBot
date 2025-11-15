package rs.kreme.ksbot.api.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rs.kreme.ksbot.api.wrappers.KSObject;

/**
 * Placeholder query for world objects.
 */
public class ObjectQuery {
    private final List<KSObject> objects;

    public ObjectQuery() {
        this(Collections.emptyList());
    }

    public ObjectQuery(List<KSObject> objects) {
        this.objects = new ArrayList<>(objects);
    }

    public List<KSObject> results() {
        return Collections.unmodifiableList(objects);
    }
}
