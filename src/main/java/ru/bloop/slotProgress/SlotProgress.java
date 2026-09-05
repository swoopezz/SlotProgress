package ru.bloop.slotProgress;

import org.bukkit.Bukkit;
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
    private InvLockManager lockManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        if (getConfig().getBoolean("debug", false))
            logger = getLogger();

        final int updatePeriod = getConfig().getInt("update_period", 100);

        slotConfig = new SlotConfig(getConfig());
        final Map<Integer, SlotTask> slotsSettings = slotConfig.load();

        slotsSettings.forEach((slot, task) -> {
            debug("{" + slot + ": " + task.toString() + "}", Level.INFO);
        });

        lockManager = new InvLockManager(slotsSettings);

        getServer().getPluginManager().registerEvents(new LockItemClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinLock(slotsSettings), this);


        debug("Starting players' inventories update task", Level.INFO);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                lockManager.updatePlayer(player);
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
}
