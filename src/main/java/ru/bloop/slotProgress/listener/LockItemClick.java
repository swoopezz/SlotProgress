package ru.bloop.slotProgress.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.bloop.slotProgress.item.LockItem;

public class LockItemClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!LockItem.isItem(event.getCurrentItem())) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        event.setCancelled(true);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
    }
}
