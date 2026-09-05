package ru.bloop.slotProgress.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.bloop.slotProgress.item.LockItem;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(LockItem::isItem);
    }
}
