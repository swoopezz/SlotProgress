package ru.bloop.slotProgress.item;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class LockItem {

    public static final Component NAME = MiniMessage.miniMessage().deserialize("<!i><red>Недоступно");
    public static final Material MATERIAL = Material.BARRIER;
    public static final NamespacedKey NK = new NamespacedKey("slot_progress", "lock_item");
    public static final int CUSTOM_MD = 9492;

    public static ItemStack toItemStack(List<Component> lore) {
        final ItemStack item = new ItemStack(MATERIAL);
        final ItemMeta meta = item.getItemMeta();

        meta.lore(lore);
        meta.displayName(NAME);
        meta.setMaxStackSize(1);
        //noinspection deprecation
        meta.setCustomModelData(CUSTOM_MD);
        meta.setEnchantmentGlintOverride(true);
        meta.getPersistentDataContainer().set(NK, PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isItem(ItemStack another) {
        if (another == null) return false;
        if (another.getItemMeta() == null) return false;
        return another.getItemMeta().getPersistentDataContainer().has(NK);
    }
}
