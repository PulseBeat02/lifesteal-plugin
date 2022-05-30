package io.github.pulsebeat02.lifesteal.listener;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.hearts.HeartManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerDeathListener implements Listener {

  private final Lifesteal lifesteal;

  public PlayerDeathListener(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
  }

  @EventHandler
  public void onPlayerDeath(@NotNull final PlayerDeathEvent event) {

    final Player killed = event.getPlayer();
    final Player killer = killed.getKiller();

    if (killer == null) {
      return;
    }

    final HeartManager manager = lifesteal.getManager();
    manager.modifyPlayers(killer.getUniqueId(), killed.getUniqueId());
  }
}
