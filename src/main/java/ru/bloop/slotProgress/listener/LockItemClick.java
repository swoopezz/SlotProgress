package ru.bloop.slotProgress.listener;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ru.bloop.slotProgress.item.LockItem;

public class LockItemClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!LockItem.isItem(event.getCurrentItem())) return;

        event.setCancelled(true);
        event.getWhoClicked().getWorld().playSound(
            event.getWhoClicked().getEyeLocation(),
            Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1
        );
    }
}
