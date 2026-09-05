package ru.bloop.slotProgress.task.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class KillEntityTask extends StatisticTask {

    private final EntityType type;

    public KillEntityTask(EntityType type, int amount) {
        super(Statistic.KILL_ENTITY, amount);
        this.type = type;
    }

    @Override
    public int completedValue(Player player) {
        return player.getStatistic(Statistic.KILL_ENTITY, type);
    }

    @Override
    public boolean isCompleted(Player player) {
        return getReqAmount() >= player.getStatistic(statistic, type);
    }

    @Override
    public List<Component> lore(int completed) {
        return List.of(
            MiniMessage.miniMessage().deserialize("<!i><gray>Убить " + completed + "/" + getReqAmount() + " " + type.translationKey())
        );
    }

    @Override
    public String toString() {
        return "KillEntityTask{" +
                "type=" + type +
                ", statistic=" + statistic +
                '}';
    }
}
