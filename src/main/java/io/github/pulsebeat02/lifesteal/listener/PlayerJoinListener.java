package io.github.pulsebeat02.lifesteal.listener;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.hearts.HeartManager;
import io.github.pulsebeat02.lifesteal.utils.PlayerUtils;
import java.util.Map;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public record PlayerJoinListener(Lifesteal lifesteal) implements
    Listener {

  public PlayerJoinListener(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
  }

  @EventHandler
  public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {

    final HeartManager manager = this.lifesteal.getManager();
    final Map<UUID, Double> map = manager.getHeartsMap();
    final Player player = event.getPlayer();
    final UUID uuid = player.getUniqueId();

    if (!PlayerUtils.isCorrectWorld(uuid)) {
      return;
    }

    if (!map.containsKey(uuid)) {
      manager.addNewPlayer(uuid);
      return;
    }

    final double hp = map.get(uuid);
    if (hp <= 0) {
      player.setGameMode(GameMode.SPECTATOR);
    }
  }
}
