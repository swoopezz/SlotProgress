package ru.bloop.slotProgress.lock;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.bloop.slotProgress.item.LockItem;
import ru.bloop.slotProgress.task.api.SlotTask;
import ru.bloop.slotProgress.task.impl.StatisticTask;

import java.util.Map;
import java.util.Set;

public class InvLockManager {

    private final Map<Integer, SlotTask> tasks;

    public InvLockManager(Map<Integer, SlotTask> tasks) {
        this.tasks = tasks;
    }

    public void updatePlayer(Player player, Inventory inv, Set<Integer> slots) {
        for (int slot : slots) {
            if (slot >= inv.getSize()) continue;

            final SlotTask task = tasks.get(slot);

            if (task == null) continue;
            if (task.isCompleted(player))  {
                if (LockItem.isItem(inv.getItem(slot))) {
                    inv.clear(slot);
                }
                continue;
            }
            // TODO rewrite lore logic
            if (task instanceof StatisticTask statTask) {
                inv.setItem(slot, LockItem.toItemStack(
                    task.lore(statTask.completedValue(player))
                ));
            } else {
                inv.setItem(slot, LockItem.toItemStack(
                    task.lore(-1)
                ));
            }
        }
    }
}
