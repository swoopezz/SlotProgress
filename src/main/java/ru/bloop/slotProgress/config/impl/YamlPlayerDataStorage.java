package ru.bloop.slotProgress.config.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.bloop.slotProgress.SlotProgress;
import ru.bloop.slotProgress.config.api.PlayerDataStorage;
import ru.bloop.slotProgress.lock.InvLocker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class YamlPlayerDataStorage implements PlayerDataStorage {

    private static final String PLAYERS_DATA_PATH = "players_data";
    private static final String FILE_NAME = "players_data.yml";
    private static final String UNLOCKED_PATH = "unlocked";

    private final JavaPlugin plugin;
    private final File dataFile;
    private YamlConfiguration config;

    public YamlPlayerDataStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), FILE_NAME);
        loadConfig();
    }

    private void loadConfig() {
        if (!dataFile.exists()) {
            SlotProgress.debug("Creating players data file...", Level.INFO);
            plugin.saveResource(FILE_NAME, false);
        }

        config = new YamlConfiguration();
        try {
            config.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            SlotProgress.debug("Failed to load players data file!", Level.SEVERE);
            throw new RuntimeException("Cannot load player data storage", e);
        }
    }

    private void saveConfig() {
        try {
            config.save(dataFile);
        } catch (IOException e) {
            SlotProgress.debug("Failed to save players data file!", Level.SEVERE);
            throw new RuntimeException("Cannot save player data storage", e);
        }
    }

    @Override
    @Nonnull
    public InvLocker load(@Nonnull Player player) {
        final UUID uuid = player.getUniqueId();
        ConfigurationSection root = config.getConfigurationSection(PLAYERS_DATA_PATH);
        if (root == null) {
            root = config.createSection(PLAYERS_DATA_PATH);
        }

        final String playerKey = uuid.toString();
        final Set<Integer> unlocked = new HashSet<>();

        if (root.contains(playerKey)) {
            final ConfigurationSection playerSection = root.getConfigurationSection(playerKey);
            if (playerSection != null) {
                List<Integer> list = playerSection.getIntegerList(UNLOCKED_PATH);
                unlocked.addAll(list);
            }
        } else {
            root.createSection(playerKey);
            saveConfig();
        }

        return new InvLocker(player, unlocked);
    }

    @Override
    public boolean save(@Nonnull InvLocker locker) {
        final Player player = locker.getPlayer();
        if (player == null) {
            SlotProgress.debug("Attempt to save InvLocker with null player – skipped.", Level.WARNING);
            return false;
        }

        final UUID uuid = player.getUniqueId();
        ConfigurationSection root = config.getConfigurationSection(PLAYERS_DATA_PATH);
        if (root == null) {
            root = config.createSection(PLAYERS_DATA_PATH);
        }

        ConfigurationSection playerSection = root.getConfigurationSection(uuid.toString());
        if (playerSection == null) {
            playerSection = root.createSection(uuid.toString());
        }

        final Set<Integer> unlocked = locker.getUnlocked();
        if (unlocked != null) {
            playerSection.set(UNLOCKED_PATH, List.copyOf(unlocked));
        } else {
            playerSection.set(UNLOCKED_PATH, null);
        }

        saveConfig();
        return true;
    }
}
