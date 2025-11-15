package rs.kreme.ksbot.api.scripts.task;

/**
 * Simple task abstraction used by {@link TaskScript}.
 */
public abstract class Task {
    /**
     * Determines whether the task should run.
     */
    public abstract boolean validate();

    /**
     * Executes the task and returns the desired delay (ms) before the next tick.
     */
    public abstract int execute();
}
