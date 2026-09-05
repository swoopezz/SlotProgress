package ru.bloop.slotProgress.lock;

import org.bukkit.entity.Player;
import ru.bloop.slotProgress.item.LockItem;
import ru.bloop.slotProgress.task.api.SlotTask;
import ru.bloop.slotProgress.task.impl.StatisticTask;

import java.util.Map;

public class InvLockManager {

    private final Map<Integer, SlotTask> tasks;

    public InvLockManager(Map<Integer, SlotTask> tasks) {
        this.tasks = tasks;
    }

    public void updatePlayer(Player player) {
        for (int slot = 9; slot < 36; ++slot) {
            final SlotTask task = tasks.get(slot);

            if (task == null) continue;
            if (task.isCompleted(player))  {
                if (LockItem.isItem(player.getInventory().getItem(slot))) {
                    player.getInventory().clear(slot);
                }
                continue;
            }
            // TODO rewrite lore logic
            if (task instanceof StatisticTask statTask) {
                player.getInventory().setItem(slot, LockItem.toItemStack(
                        task.lore(statTask.completedValue(player))
                ));
            } else {
                player.getInventory().setItem(slot, LockItem.toItemStack(
                        task.lore(-1)
                ));
            }
        }
    }
}
