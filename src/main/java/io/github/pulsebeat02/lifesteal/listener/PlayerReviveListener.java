package io.github.pulsebeat02.lifesteal.listener;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.utils.ItemStackUtils;
import io.github.pulsebeat02.lifesteal.utils.PlayerUtils;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class PlayerReviveListener implements Listener {

  private final Lifesteal lifesteal;

  public PlayerReviveListener(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
  }

  @EventHandler
  public void onRightClick(@NotNull final PlayerInteractEvent event) {

    final Action action = event.getAction();
    final Player player = event.getPlayer();
    final UUID uuid = player.getUniqueId();

    if (!PlayerUtils.isCorrectWorld(uuid)) {
      return;
    }

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

    if (!ItemStackUtils.isValidPlaceableHead(item)) {
      return;
    }

    player.openInventory(this.lifesteal.getGui().getInventory());
  }
}
