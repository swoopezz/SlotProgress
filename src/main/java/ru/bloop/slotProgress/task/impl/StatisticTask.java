package ru.bloop.slotProgress.task.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import ru.bloop.slotProgress.task.api.SlotTask;

import java.util.List;

    public class StatisticTask extends SlotTask {

        protected final Statistic statistic;

        public StatisticTask(Statistic statistic, int amount) {
            super(amount);
            this.statistic = statistic;
        }

        public int completedValue(Player player) {
            return player.getStatistic(statistic);
        }

        @Override
        public boolean isCompleted(Player player) {
            return player.getStatistic(statistic) >= getReqAmount();
        }

        @Override
        public List<Component> lore(int completed) {
            return List.of(
                MiniMessage.miniMessage().deserialize("<!i><gray>Сделать " + completed + "/" + getReqAmount() + " " + statistic.name().toLowerCase())
            );
        }

        @Override
        public String toString() {
            return "StatisticTask{" +
                   "statistic=" + statistic +
                   "requeried=" + getReqAmount() +
                   '}';
        }
    }
