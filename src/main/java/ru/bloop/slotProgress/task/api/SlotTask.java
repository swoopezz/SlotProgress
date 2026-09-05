package ru.bloop.slotProgress.task.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SlotTask {

    private final int requiredAmount;

    protected SlotTask(int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public abstract boolean isCompleted(Player player);
    public abstract List<Component> lore(int completed);

    public int getReqAmount() {
        return requiredAmount;
    }

    @Override
    public String toString() {
        return "SlotTask{" +
                "requiredAmount=" + requiredAmount +
                '}';
    }
}
