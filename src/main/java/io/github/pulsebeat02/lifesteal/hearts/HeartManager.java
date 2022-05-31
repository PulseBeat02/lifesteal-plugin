package io.github.pulsebeat02.lifesteal.hearts;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.locale.Locale;
import io.github.pulsebeat02.lifesteal.locale.Locale.NullComponent;
import io.github.pulsebeat02.lifesteal.locale.Sender;
import io.github.pulsebeat02.lifesteal.utils.ItemStackUtils;
import io.github.pulsebeat02.lifesteal.utils.PlayerUtils;
import io.github.pulsebeat02.lifesteal.utils.SkullCreator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class HeartManager {

  private final Lifesteal lifesteal;
  private final Map<UUID, Double> hearts;
  private final BukkitAudiences audiences;

  public HeartManager(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
    this.hearts = new HashMap<>();
    this.audiences = lifesteal.getAudiences();
  }

  public void addExistingPlayer(@NotNull final UUID uuid, final double health) {
    this.hearts.put(uuid, health);
  }

  public void deleteExistingPlayer(@NotNull final UUID uuid) {
    this.hearts.remove(uuid);
  }

  public void addNewPlayer(@NotNull final UUID uuid) {
    this.hearts.put(uuid, 20D);
  }

  public void modifyPlayers(@NotNull final UUID killer, @NotNull final UUID killed) {
    this.addHeart(killer);
    this.subtractHeart(killed);
    this.checkZero(killed);
    this.save();
  }

  private void save() {
    try {
      this.lifesteal.getPersistentStorage().save();
    } catch (final IOException e) {
      throw new AssertionError(e);
    }
  }

  private void addHeart(@NotNull final UUID uuid) {
    this.modifyHeart(uuid, Locale.PLAYER_GAIN_HEART, 1D);
  }

  private void subtractHeart(@NotNull final UUID uuid) {
    this.modifyHeart(uuid, Locale.PLAYER_LOSE_HEART, -1D);
  }

  private void checkZero(@NotNull final UUID uuid) {
    final double value = this.hearts.get(uuid);
    if (value <= 0) {
      final Player player = PlayerUtils.getPlayerExceptionally(uuid);
      this.sendZeroHeartMessage(player);
      this.setSpectator(player);
      this.dropHead(uuid);
    }
  }

  private void sendZeroHeartMessage(@NotNull final Player player) {
    final Audience audience = this.audiences.player(player);
    audience.sendMessage(Locale.PLAYER_ZERO_HEARTS.build());
  }

  private void setSpectator(@NotNull final Player player) {
    player.setGameMode(GameMode.SPECTATOR);
  }

  private void dropHead(@NotNull final UUID uuid) {
    final Runnable runnable = () -> this.createItemStack(uuid);
    ForkJoinPool.commonPool().execute(runnable);
  }

  private void createItemStack(@NotNull final UUID uuid) {
    final ItemStack head = SkullCreator.itemFromUuid(uuid);
    final ItemStack modified = ItemStackUtils.addNBTData(head, uuid);
    this.dropItem(uuid, modified);
  }

  private void dropItem(@NotNull final UUID uuid, @NotNull final ItemStack head) {
    if (!ItemStackUtils.dropItemAtPlayer(uuid, head)) {
      throw new AssertionError("Head for UUID %s despawned!".formatted(uuid));
    }
  }

  private void modifyHeart(
      @NotNull final UUID uuid,
      @NotNull final NullComponent<Sender> component,
      final double change) {

    double changed = this.hearts.get(uuid) + change;
    if (changed < 0) {
      changed = 0;
    }

    this.hearts.put(uuid, changed);

    this.setInGameHearts(uuid);
    this.sendMessage(uuid, component);
  }

  private void sendMessage(
      @NotNull final UUID uuid, @NotNull final NullComponent<Sender> component) {
    final Player player = PlayerUtils.getPlayerExceptionally(uuid);
    this.audiences.player(player).sendMessage(component.build());
  }

  private void setInGameHearts(@NotNull final UUID uuid) {
    final double health = this.hearts.get(uuid);
    PlayerUtils.setHealth(uuid, health);
  }

  public @NotNull Map<UUID, Double> getHeartsMap() {
    return this.hearts;
  }
}
