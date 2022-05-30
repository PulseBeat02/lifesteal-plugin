package io.github.pulsebeat02.lifesteal;

import io.github.pulsebeat02.lifesteal.gui.HeadPlacementGui;
import io.github.pulsebeat02.lifesteal.hearts.HeartManager;
import io.github.pulsebeat02.lifesteal.listener.PlayerDeathListener;
import io.github.pulsebeat02.lifesteal.listener.PlayerGuiPlacementListener;
import io.github.pulsebeat02.lifesteal.listener.PlayerReviveListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Lifesteal extends JavaPlugin {

  private BukkitAudiences audiences;
  private HeadPlacementGui gui;
  private HeartManager manager;

  @Override
  public void onEnable() {
    this.audiences = BukkitAudiences.create(this);
    this.manager = new HeartManager(this);
    this.gui = new HeadPlacementGui();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  private void registerListeners() {

    final Server server = getServer();
    final PluginManager manager = server.getPluginManager();

    final PlayerDeathListener playerDeathListener = new PlayerDeathListener(this);
    final PlayerGuiPlacementListener playerGuiPlacementListener =
        new PlayerGuiPlacementListener(this);
    final PlayerReviveListener playerReviveListener = new PlayerReviveListener(this);

    manager.registerEvents(playerDeathListener, this);
    manager.registerEvents(playerGuiPlacementListener, this);
    manager.registerEvents(playerReviveListener, this);
  }

  public @NotNull BukkitAudiences getAudiences() {
    return audiences;
  }

  public @NotNull HeartManager getManager() {
    return manager;
  }

  public @NotNull HeadPlacementGui getGui() {
    return gui;
  }
}
