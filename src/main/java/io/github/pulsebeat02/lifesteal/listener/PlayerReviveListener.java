package io.github.pulsebeat02.lifesteal.listener;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.key.NamespacedKeyProvider;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class PlayerReviveListener implements Listener {

  private final Lifesteal lifesteal;

  public PlayerReviveListener(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
  }

  @EventHandler
  public void onRightClick(@NotNull final PlayerInteractEvent event) {

    final Action action = event.getAction();

    if (!(action == Action.RIGHT_CLICK_AIR || action.equals(Action.RIGHT_CLICK_BLOCK))) {
      return;
    }

    final ItemStack item = event.getItem();

    if (item == null) {
      return;
    }

    if (item.getType() == Material.AIR) {
      return;
    }

    if (!this.isValidPlaceableHead(item)) {
      return;
    }

    event.getPlayer().openInventory(this.lifesteal.getGui().getInventory());
  }

  public boolean isValidPlaceableHead(@NotNull final ItemStack stack) {
    final ItemMeta meta = stack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    return container.has(NamespacedKeyProvider.OWNER_UUID, PersistentDataType.INTEGER);
  }
}
