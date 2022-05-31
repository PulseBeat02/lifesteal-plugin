package io.github.pulsebeat02.lifesteal;

import io.github.pulsebeat02.lifesteal.gui.HeadPlacementGui;
import io.github.pulsebeat02.lifesteal.hearts.HeartManager;
import io.github.pulsebeat02.lifesteal.listener.PlayerDeathListener;
import io.github.pulsebeat02.lifesteal.listener.PlayerGuiPlacementListener;
import io.github.pulsebeat02.lifesteal.listener.PlayerReviveListener;
import io.github.pulsebeat02.lifesteal.persistent.HeartPersistentStorage;
import java.io.IOException;
import java.util.Set;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Lifesteal extends JavaPlugin {

  private BukkitAudiences audiences;
  private HeartPersistentStorage persistentStorage;
  private HeadPlacementGui gui;
  private HeartManager manager;

  @Override
  public void onEnable() {
    this.initializeAudience();
    this.initializeManager();
    this.parseHeartData();
    this.registerListeners();
    this.initializeHeadPlacementGui();
  }

  @Override
  public void onDisable() {
    this.saveHeartData();
  }

  private void initializeAudience() {
    this.audiences = BukkitAudiences.create(this);
  }

  private void initializeManager() {
    this.manager = new HeartManager(this);
  }

  private void parseHeartData() {
    this.persistentStorage = new HeartPersistentStorage(this);
    try {
      this.persistentStorage.read();
    } catch (@NotNull final IOException e) {
      throw new AssertionError(e);
    }
  }

  private void initializeHeadPlacementGui() {
    this.gui = new HeadPlacementGui();
  }

  private void saveHeartData() {
    try {
      this.persistentStorage.save();
    } catch (final IOException e) {
      throw new AssertionError(e);
    }
  }

  private void registerListeners() {

    final Server server = this.getServer();
    final PluginManager manager = server.getPluginManager();

    final Set<Listener> listeners =
        Set.of(
            new PlayerDeathListener(this),
            new PlayerGuiPlacementListener(this),
            new PlayerReviveListener(this));
    listeners.forEach((listener) -> manager.registerEvents(listener, this));
  }

  public @NotNull BukkitAudiences getAudiences() {
    return this.audiences;
  }

  public @NotNull HeartManager getManager() {
    return this.manager;
  }

  public @NotNull HeadPlacementGui getGui() {
    return this.gui;
  }

  public @NotNull HeartPersistentStorage getPersistentStorage() {
    return this.persistentStorage;
  }
}
