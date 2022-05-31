package io.github.pulsebeat02.lifesteal.listener;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.hearts.HeartManager;
import io.github.pulsebeat02.lifesteal.key.NamespacedKeyProvider;
import io.github.pulsebeat02.lifesteal.utils.PlayerUtils;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public record PlayerGuiPlacementListener(
    Lifesteal lifesteal) implements Listener {

  public PlayerGuiPlacementListener(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
  }

  @EventHandler
  public void onGuiButtonClick(@NotNull final InventoryClickEvent event) {

    final HumanEntity clicker = event.getWhoClicked();
    if (!(clicker instanceof Player)) {
      return;
    }

    final Player player = (Player) clicker;
    final UUID uuid = player.getUniqueId();
    if (!PlayerUtils.isCorrectWorld(uuid)) {
      return;
    }

    final Inventory inventory = this.lifesteal.getGui().getInventory();
    if (inventory != event.getInventory()) {
      return;
    }

    final HeartManager manager = this.lifesteal.getManager();
    final int slot = event.getSlot();
    switch (slot) {
      case 11 -> manager.modifyPlayers(clicker.getUniqueId(), this.getRevivedPlayerUUID(event));
      case 15 -> clicker.closeInventory();
    }
  }

  private @NotNull
  UUID getRevivedPlayerUUID(@NotNull final InventoryClickEvent event) {

    final Player owner = (Player) event.getWhoClicked();
    final PlayerInventory inventory = owner.getInventory();

    final ItemStack main = inventory.getItemInMainHand(); // user could've right-clicked if the item is offhand or normal hand
    final ItemStack offhand = inventory.getItemInOffHand();

    final Optional<String> mainOptional = this.extractUUID(main);
    final Optional<String> offhandOptional = this.extractUUID(offhand);

    return UUID.fromString(mainOptional.orElseGet(offhandOptional::get));
  }

  private @NotNull
  Optional<String> extractUUID(@NotNull final ItemStack stack) {

    final ItemMeta meta = stack.getItemMeta();

    final PersistentDataContainer container = meta.getPersistentDataContainer();
    return Optional.ofNullable(
        container.get(NamespacedKeyProvider.OWNER_UUID, PersistentDataType.STRING));
  }
}
