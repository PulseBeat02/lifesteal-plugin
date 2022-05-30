package io.github.pulsebeat02.lifesteal.gui;

import io.github.pulsebeat02.lifesteal.locale.Locale;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class HeadPlacementGui {

  private final Inventory inventory;

  public HeadPlacementGui() {
    this.inventory = Bukkit.createInventory(null, 27, Locale.SMP_REVIVE_GUI_TITLE.build());
    initialize();
  }

  private void initialize() {
    inventory.setItem(11, getConfirmationButton());
    inventory.setItem(15, getCancelButton());
  }

  private @NotNull ItemStack getConfirmationButton() {
    final ItemStack confirm = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
    final ItemMeta meta = confirm.getItemMeta();
    meta.displayName(Locale.SMP_REVIVE_GUI_CONFIRM_NAME.build());
    meta.lore(List.of(Locale.SMP_REVIVE_GUI_CONFIRM_LORE.build()));
    confirm.setItemMeta(meta);
    return confirm;
  }

  private @NotNull ItemStack getCancelButton() {
    final ItemStack cancel = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    final ItemMeta meta = cancel.getItemMeta();
    meta.displayName(Locale.SMP_REVIVE_GUI_CANCEL_NAME.build());
    meta.lore(List.of(Locale.SMP_REVIVE_GUI_CANCEL_LORE.build()));
    cancel.setItemMeta(meta);
    return cancel;
  }

  public @NotNull Inventory getInventory() {
    return inventory;
  }
}
