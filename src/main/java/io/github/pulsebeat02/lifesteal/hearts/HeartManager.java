package io.github.pulsebeat02.lifesteal.hearts;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.key.NamespacedKeyProvider;
import io.github.pulsebeat02.lifesteal.locale.Locale;
import io.github.pulsebeat02.lifesteal.locale.Locale.NullComponent;
import io.github.pulsebeat02.lifesteal.locale.Sender;
import io.github.pulsebeat02.lifesteal.utils.SkullCreator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class HeartManager {

  private final Lifesteal lifesteal;
  private final Map<UUID, Double> hearts;

  public HeartManager(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
    this.hearts = new HashMap<>();
  }

  public void addExistingPlayer(@NotNull final UUID uuid, final double health) {
    hearts.put(uuid, health);
  }

  public void deleteExistingPlayer(@NotNull final UUID uuid) {
    hearts.remove(uuid);
  }

  public void addNewPlayer(@NotNull final UUID uuid) {
    hearts.put(uuid, 20D);
  }

  public void modifyPlayers(@NotNull final UUID killer, @NotNull final UUID killed) {
    addHeart(killer);
    subtractHeart(killed);
    checkZero(killed);
  }

  public void sacrificeHeart(@NotNull final UUID sacrificer, @NotNull final UUID dead) {
    addHeart(dead);
    subtractHeart(sacrificer);
    checkZero(sacrificer);
  }

  private void addHeart(@NotNull final UUID uuid) {
    modifyHeart(uuid, Locale.PLAYER_GAIN_HEART, 1D);
  }

  private void subtractHeart(@NotNull final UUID uuid) {
    modifyHeart(uuid, Locale.PLAYER_LOSE_HEART, -1D);
  }

  private void checkZero(@NotNull final UUID uuid) {
    final double value = hearts.get(uuid);
    if (value <= 0) {
      final Player player = getPlayer(uuid);
      sendZeroHeartMessage(player);
      setSpectator(player);
      dropHead(player);
    }
  }

  private void sendZeroHeartMessage(@NotNull final Player player) {
    final BukkitAudiences audiences = lifesteal.getAudiences();
    final Audience audience = audiences.player(player);
    audience.sendMessage(Locale.PLAYER_ZERO_HEARTS.build());
  }

  private void setSpectator(@NotNull final Player player) {
    player.setGameMode(GameMode.SPECTATOR);
  }

  private void dropHead(@NotNull final Player player) {
    CompletableFuture.runAsync(
        () -> {
          final ItemStack head = SkullCreator.itemFromUuid(player.getUniqueId());
          final ItemStack modified = addNBTData(head, player.getUniqueId());
          dropItem(player, modified);
        });
  }

  private void dropItem(@NotNull final Player player, @NotNull final ItemStack head) {
    final Location location = player.getLocation();
    final World world = location.getWorld();
    world.dropItem(location, head);
  }

  private @NotNull ItemStack addNBTData(@NotNull final ItemStack stack, @NotNull final UUID uuid) {
    final ItemMeta meta = stack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    container.set(NamespacedKeyProvider.PLACEABLE_HEAD, PersistentDataType.INTEGER, 727);
    container.set(NamespacedKeyProvider.OWNER_UUID, PersistentDataType.STRING, uuid.toString());
    stack.setItemMeta(meta);
    return stack;
  }

  private void modifyHeart(
      @NotNull final UUID uuid,
      @NotNull final NullComponent<Sender> component,
      final double change) {
    final double changed = hearts.get(uuid) + change;
    hearts.put(uuid, changed);
    setInGameHearts(uuid);
    sendMessage(uuid, component);
  }

  private void sendMessage(
      @NotNull final UUID uuid, @NotNull final NullComponent<Sender> component) {
    final BukkitAudiences audience = lifesteal.getAudiences();
    final Player player = getPlayer(uuid);
    audience.player(player).sendMessage(component.build());
  }

  private void setInGameHearts(@NotNull final UUID uuid) {
    final double health = hearts.get(uuid);
    final Player player = getPlayer(uuid);
    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
    player.setHealth(health);
  }

  private @NotNull Player getPlayer(@NotNull final UUID uuid) {
    final Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      throw new AssertionError("The player %s is not online!".formatted(uuid));
    }
    return player;
  }
}
