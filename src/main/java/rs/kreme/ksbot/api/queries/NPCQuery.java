package rs.kreme.ksbot.api.queries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rs.kreme.ksbot.api.wrappers.KSNPC;

/**
 * Simple NPC query holder.
 */
public class NPCQuery {
    private final List<KSNPC> npcs;

    public NPCQuery() {
        this(Collections.emptyList());
    }

    public NPCQuery(List<KSNPC> npcs) {
        this.npcs = new ArrayList<>(npcs);
    }

    public List<KSNPC> results() {
        return Collections.unmodifiableList(npcs);
    }
}
