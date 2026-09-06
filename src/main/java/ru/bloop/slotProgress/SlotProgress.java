package ru.bloop.slotProgress;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import ru.bloop.slotProgress.config.SlotConfig;
import ru.bloop.slotProgress.listener.LockItemClick;
import ru.bloop.slotProgress.listener.PlayerDeathListener;
import ru.bloop.slotProgress.listener.PlayerJoinLock;
import ru.bloop.slotProgress.lock.InvLockManager;
import ru.bloop.slotProgress.task.api.SlotTask;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SlotProgress extends JavaPlugin {

    private static Logger logger;
    private SlotConfig slotConfig;
    private SlotConfig endChestConfig;
    private InvLockManager lockManager;
    private InvLockManager ecLockManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        if (getConfig().getBoolean("debug", false))
            logger = getLogger();

        final int updatePeriod = getConfig().getInt("update_period", 100);

        slotConfig = new SlotConfig("unlock_quests", getConfig());
        endChestConfig = new SlotConfig("ender_chest_quests", getConfig());

        final Map<Integer, SlotTask> slotsSettings = slotConfig.load(9, 36);
        final Map<Integer, SlotTask> ecSlotsSettings = endChestConfig.load(0, 27);

        debugSlotTasks(slotsSettings, "Players' main inventories tasks");
        debug("\n", Level.INFO);
        debugSlotTasks(ecSlotsSettings, "Players' ender chests tasks");

        lockManager = new InvLockManager(slotsSettings);
        ecLockManager = new InvLockManager(ecSlotsSettings);

        getServer().getPluginManager().registerEvents(new LockItemClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinLock(slotsSettings), this);

        debug("Starting players' inventories update task", Level.INFO);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                lockManager.updatePlayer(player, player.getInventory(), slotsSettings.keySet());
                ecLockManager.updatePlayer(player, player.getEnderChest(), ecSlotsSettings.keySet());
            });
        }, 100, updatePeriod);

    }

    @Override
    public void onDisable() {
    }

    public static void debug(String text, Level level) {
        if (logger == null) return;
        logger.log(level, text);
    }

    private void debugSlotTasks(Map<Integer, SlotTask> tasks, String additional) {
        debug(additional, Level.INFO);

        tasks.forEach((slot, task) -> {
            debug("{" + slot + ": " + task.toString() + "}", Level.INFO);
        });
    }
}
