package io.github.pulsebeat02.lifesteal.gui;

import io.github.pulsebeat02.lifesteal.locale.Locale;
import io.github.pulsebeat02.lifesteal.locale.Locale.NullComponent;
import io.github.pulsebeat02.lifesteal.locale.Sender;
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
    this.initialize();
  }

  private void initialize() {
    this.inventory.setItem(11, this.getConfirmationButton());
    this.inventory.setItem(15, this.getCancelButton());
  }

  private @NotNull ItemStack getConfirmationButton() {
    return this.getButton(
        Material.GREEN_STAINED_GLASS_PANE,
        Locale.SMP_REVIVE_GUI_CONFIRM_NAME,
        Locale.SMP_REVIVE_GUI_CONFIRM_LORE);
  }

  private @NotNull ItemStack getCancelButton() {
    return this.getButton(
        Material.RED_STAINED_GLASS_PANE,
        Locale.SMP_REVIVE_GUI_CANCEL_NAME,
        Locale.SMP_REVIVE_GUI_CANCEL_LORE);
  }

  private @NotNull ItemStack getButton(
      @NotNull final Material material,
      @NotNull final NullComponent<Sender> name,
      @NotNull final NullComponent<Sender> lore) {

    final ItemStack button = new ItemStack(material);

    final ItemMeta meta = button.getItemMeta();
    meta.displayName(name.build());
    meta.lore(List.of(lore.build()));
    button.setItemMeta(meta);

    return button;
  }

  public @NotNull Inventory getInventory() {
    return this.inventory;
  }
}
