package ru.bloop.slotProgress.config.api;

import org.bukkit.entity.Player;
import ru.bloop.slotProgress.lock.InvLocker;

public interface PlayerDataStorage {

    InvLocker load(Player player);

    boolean save(InvLocker locker);
}
