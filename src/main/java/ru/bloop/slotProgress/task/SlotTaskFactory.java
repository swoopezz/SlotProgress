package ru.bloop.slotProgress.task;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import ru.bloop.slotProgress.task.api.SlotTask;
import ru.bloop.slotProgress.task.impl.BreakBlockTask;
import ru.bloop.slotProgress.task.impl.KillEntityTask;
import ru.bloop.slotProgress.task.impl.StatisticTask;


public class SlotTaskFactory {

    public static SlotTask createStatistic(Statistic stat, int reqAmount) {
        return new StatisticTask(stat, reqAmount);
    }

    public static SlotTask createBreakBlock(Material material, int reqAmount) {
        return new BreakBlockTask(material, reqAmount);
    }

    public static SlotTask createKillEntity(EntityType type, int reqAmount) {
        return new KillEntityTask(type, reqAmount);
    }

}
