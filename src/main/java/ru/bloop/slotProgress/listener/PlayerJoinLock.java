package ru.bloop.slotProgress.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.bloop.slotProgress.config.api.PlayerDataStorage;
import ru.bloop.slotProgress.item.LockItem;
import ru.bloop.slotProgress.lock.InvLocker;
import ru.bloop.slotProgress.task.api.SlotTask;
import ru.bloop.slotProgress.task.impl.StatisticTask;

import java.util.Map;

public class PlayerJoinLock implements Listener {
    private final PlayerDataStorage storage;
    private final Map<Integer, SlotTask> tasks;

    public PlayerJoinLock(PlayerDataStorage storage, Map<Integer, SlotTask> tasks) {
        this.storage = storage;
        this.tasks = tasks;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final InvLocker locker = storage.load(event.getPlayer());
        final Player player = event.getPlayer();

        for (int slot = 9; slot < 36; ++slot) {
            if (locker.getUnlocked().contains(slot)) continue;
            final SlotTask task = tasks.get(slot);
            if (task == null) continue;
            if (task.isCompleted(event.getPlayer())) continue;

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
