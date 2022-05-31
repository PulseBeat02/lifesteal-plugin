package io.github.pulsebeat02.lifesteal.listener;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.hearts.HeartManager;
import io.github.pulsebeat02.lifesteal.utils.PlayerUtils;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public record PlayerDeathListener(Lifesteal lifesteal) implements
    Listener {

  public PlayerDeathListener(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
  }

  @EventHandler
  public void onPlayerDeath(@NotNull final PlayerDeathEvent event) {

    final Player victim = event.getPlayer();
    final Player killer = victim.getKiller();

    if (killer == null) {
      return;
    }

    final UUID victimUUID = victim.getUniqueId();
    final UUID killerUUID = killer.getUniqueId();

    if (!PlayerUtils.isCorrectWorld(victimUUID)) {
      return;
    }

    if (!PlayerUtils.isCorrectWorld(killerUUID)) {
      return;
    }

    final HeartManager manager = this.lifesteal.getManager();
    manager.modifyPlayers(killer.getUniqueId(), victim.getUniqueId());
  }
}
