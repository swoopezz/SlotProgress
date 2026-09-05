package ru.bloop.slotProgress.config;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import ru.bloop.slotProgress.SlotProgress;
import ru.bloop.slotProgress.task.SlotTaskFactory;
import ru.bloop.slotProgress.task.TaskType;
import ru.bloop.slotProgress.task.api.SlotTask;
import ru.bloop.slotProgress.task.impl.BreakBlockTask;

import java.util.*;
import java.util.logging.Level;

public class SlotConfig {

    public static final String SLOT_CONFIG_FIELD = "unlock_quests";
    public static final Set<Integer> LOCKABLE_SLOTS = Set.of(
        9,  10, 11, 12, 13, 14, 15, 16, 17,
        18, 19, 20, 21, 22, 23, 24, 25, 26,
        27, 28, 29, 30, 31, 32, 33, 34, 35
    );

    private final FileConfiguration config;

    public SlotConfig(FileConfiguration config) {
        this.config = config;
    }

     public Map<Integer, SlotTask> load() {
        final Map<Integer, SlotTask> result = new HashMap<>();
        final ConfigurationSection section = config.getConfigurationSection(SLOT_CONFIG_FIELD);

        if (section == null)
            return loadDefaults();

        for (String key: section.getKeys(false)) {
            int slot = 0;

            try { slot = Integer.parseInt(key); } catch (NumberFormatException e) { SlotProgress.debug("Undefined key in " + SLOT_CONFIG_FIELD + ", ignoring it.", Level.WARNING);}
            if (!LOCKABLE_SLOTS.contains(slot)) continue; // pohui

            final ConfigurationSection slotSection = section.getConfigurationSection(key);
            if (slotSection == null) continue; // also pohui
            if (!isValidSlotSection(slotSection)) {
                result.put(slot, createDefaultTask());
                SlotProgress.debug("Invalid Slot SECTION: " + slotSection + ". ", Level.WARNING);
                continue;
            } // mega pohui

            final TaskType taskType = TaskType.valueOf(slotSection.getString("type").toUpperCase());
            final SlotTask task = createTask(
                    taskType, // this bullshit valid, probably
                    loadOptionalParams(slotSection, taskType),
                    slotSection.getInt("amount", 1)
            );
            if (task == null) {
                SlotProgress.debug("Task is not created. Reason: Invalid task section", Level.INFO);
                result.put(slot, createDefaultTask());
                continue;
            } // pohui x4
            result.put(slot, task);
        }

        SlotProgress.debug("Creating default tasks", Level.INFO);
        int counter = 0;
        for (int i = 9; i < 36; ++i) {
            if (!result.containsKey(i)) {
                result.put(i, createDefaultTask());
                counter++;
            }
        }
        SlotProgress.debug("Created " + counter + " default tasks.", Level.INFO);

        return result;
     }


     public SlotTask createDefaultTask() {
        return new BreakBlockTask(Material.STONE, 10000);
     }

     public Map<Integer, SlotTask> loadDefaults() {
        final Map<Integer, SlotTask> def = new HashMap<>();
        final Random random = new Random();

        for (int slot = 9; slot < 36; slot++) {
            def.put(slot, new BreakBlockTask(Material.STONE, random.nextInt(10, 100)));
        }

        return def;
     }

     // someone, rewrite this fucking bullshit, please
     public SlotTask createTask(TaskType type, Map<String, String> opt, int amount) {
        switch (type) {
            case STATISTIC -> {
                try {
                    return SlotTaskFactory.createStatistic(Statistic.valueOf(opt.getOrDefault("statistic", "").toUpperCase()), amount);
                } catch (IllegalArgumentException e) {
                    SlotProgress.debug("undefined statistic type in config, ignore", Level.WARNING);
                    return null;
                }
            }
            case BREAK_BLOCK -> {
                try {
                    return SlotTaskFactory.createBreakBlock(Material.valueOf(opt.getOrDefault("block", "").toUpperCase()), amount);
                } catch (IllegalArgumentException e) {
                    SlotProgress.debug("undefined block type in config, ignore", Level.WARNING);
                    return null;
                }
            }
            case KILL_ENTITY -> {
                try {
                    return SlotTaskFactory.createKillEntity(EntityType.valueOf(opt.getOrDefault("entity", "").toUpperCase()), amount);
                } catch (IllegalArgumentException e) {
                    SlotProgress.debug("undefined entity type in config, ignore", Level.WARNING);
                    return null;
                }
            }
            case null, default -> { return null; }
        }
     }


     public Map<String, String> loadOptionalParams(ConfigurationSection section, TaskType type) {
        final Map<String, String> res = new HashMap<>();
        for (String key : type.getOptionalParams()) {
            res.put(key, section.getString(key, ""));
        }
        return res;
     }

     private boolean isValidSlotSection(ConfigurationSection section) {
            if (section == null) return false;

            for (String key : section.getKeys(false)) {
                SlotProgress.debug(key, Level.INFO);
            }

            if (!section.contains("type")) return false;
            if (!section.contains("amount")) return false;

         try {
             final TaskType type = TaskType.valueOf(section.getString("type", "").toUpperCase());
             return section.getKeys(false).containsAll(type.getOptionalParams());
         } catch (IllegalArgumentException e) {
             return false;
         }
     }

}
