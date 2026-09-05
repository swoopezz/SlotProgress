package ru.bloop.slotProgress.task.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.List;

public class BreakBlockTask extends StatisticTask {
    private final Material blockType;

    public BreakBlockTask(Material blockType, int amount) {
        super(Statistic.MINE_BLOCK, amount);
        this.blockType = blockType;
    }

    @Override
    public int completedValue(Player player) {
        return player.getStatistic(Statistic.MINE_BLOCK, blockType);
    }

    @Override
    public boolean isCompleted(Player player) {
        return getReqAmount() >= completedValue(player);
    }

    @Override
    public List<Component> lore(int completed) {
        return List.of(
            MiniMessage.miniMessage().deserialize("<!i><gray>Добыть " + completed + "/" + getReqAmount() + " " + blockType.translationKey())
        );
    }

    @Override
    public String toString() {
        return "BreakBlockTask{" +
                "blockType=" + blockType +
                ", statistic=" + statistic +
                ", amount= " + getReqAmount() + '}';
    }
}
