package ru.bloop.slotProgress.task;

import java.util.Set;

public enum TaskType {
    STATISTIC(Set.of("statistic")),
    BREAK_BLOCK(Set.of("block")),
    KILL_ENTITY(Set.of("entity"));

    private final Set<String> optionalParams;

    TaskType(Set<String> optionalParams) {
        this.optionalParams = optionalParams;
    }

    public Set<String> getOptionalParams() {
        return optionalParams;
    }
}
