package io.github.pulsebeat02.lifesteal.utils;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerUtils {

  private static final Set<String> VALID_WORLDS;

  static {
    VALID_WORLDS = Set.of("world", "world_nether", "world_the_end");
  }

  private PlayerUtils() {}

  public static void setHealth(@NotNull final UUID uuid, final double hp) {

    final Player player = getPlayerExceptionally(uuid);

    Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(hp);
    player.setHealth(hp);
  }

  public static @NotNull Player getPlayerExceptionally(@NotNull final UUID uuid) {

    final Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      throw new AssertionError("The player %s is not online!".formatted(uuid));
    }

    return player;
  }

  public static boolean isCorrectWorld(@NotNull final UUID uuid) {

    final Player player = getPlayerExceptionally(uuid);
    final Location location = player.getLocation();
    final World world = location.getWorld();

    return VALID_WORLDS.contains(world.getName());
  }
}
