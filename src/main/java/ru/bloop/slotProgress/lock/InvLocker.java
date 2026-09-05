package ru.bloop.slotProgress.lock;

import org.bukkit.entity.Player;
import ru.bloop.slotProgress.config.SlotConfig;
import ru.bloop.slotProgress.task.api.SlotTask;

import java.util.Set;

public class InvLocker {

    private final Player player;
    private final Set<Integer> unlocked;

    public InvLocker(Player player, Set<Integer> unlocked) {
        this.player = player;
        this.unlocked = unlocked;
    }

    public void unlock(int slot) {
        if (!SlotConfig.LOCKABLE_SLOTS.contains(slot)) return;
        unlocked.add(slot);
    }

    public void lock(int slot) {
        unlocked.remove(slot);
    }

    public Player getPlayer() {
        return player;
    }

    public Set<Integer> getUnlocked() {
        return unlocked;
    }
}
