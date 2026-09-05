package ru.bloop.slotProgress;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.bloop.slotProgress.config.SlotConfig;
import ru.bloop.slotProgress.config.api.PlayerDataStorage;
import ru.bloop.slotProgress.config.impl.YamlPlayerDataStorage;
import ru.bloop.slotProgress.item.LockItem;
import ru.bloop.slotProgress.listener.LockItemClick;
import ru.bloop.slotProgress.listener.PlayerJoinLock;
import ru.bloop.slotProgress.lock.InvLocker;
import ru.bloop.slotProgress.task.api.SlotTask;
import ru.bloop.slotProgress.task.impl.StatisticTask;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SlotProgress extends JavaPlugin {

    private static Logger logger;
    private SlotConfig slotConfig;
    private PlayerDataStorage storage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getConfig().getBoolean("debug", false))
            logger = getLogger();
        slotConfig = new SlotConfig(getConfig());
        storage = new YamlPlayerDataStorage(this);

        final Map<Integer, SlotTask> slotsSettings = slotConfig.load();
        slotsSettings.forEach((slot, task) -> {
            debug("{" + slot + ": " + task.toString() + "}", Level.INFO);
        });

        getServer().getPluginManager().registerEvents(new LockItemClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinLock(storage, slotsSettings), this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            debug("Updating players invs..", Level.INFO);
            Bukkit.getOnlinePlayers().forEach(player -> {
                updatePlayersInventory(player, slotsSettings);
            });
            debug("Players invs was updated.", Level.INFO);
        }, 100, 200);

    }

    private void updatePlayersInventory(Player player, Map<Integer, SlotTask> tasks) {
            for (int slot = 9; slot < 36; ++slot) {
                final SlotTask task = tasks.get(slot);

                if (task == null) continue;
                if (task.isCompleted(player))  {
                    debug("Task completed: " + task, Level.INFO);
                    if (LockItem.isItem(player.getInventory().getItem(slot))) {
                        player.getInventory().clear(slot);
                        debug("Remove item in slot: " + slot, Level.INFO);
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

    @Override
    public void onDisable() {
    }

    public static void debug(String text, Level level) {
        if (logger == null) return;
        logger.log(level, text);
    }
}
