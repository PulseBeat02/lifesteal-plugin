package io.github.pulsebeat02.lifesteal.utils;

import io.github.pulsebeat02.lifesteal.key.NamespacedKeyProvider;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public final class ItemStackUtils {

  private ItemStackUtils() {}

  public static boolean dropItemAtPlayer(@NotNull final UUID uuid, @NotNull final ItemStack stack) {

    final Player player = PlayerUtils.getPlayerExceptionally(uuid);
    final Location location = player.getLocation();
    final World world = location.getWorld();

    final Item item = world.dropItem(location, stack);
    item.setHealth(Integer.MAX_VALUE);
    item.setUnlimitedLifetime(true);
    item.setWillAge(false);
    item.setGlowing(true);
    item.setInvulnerable(true);

    return item.isValid();
  }

  public static ItemStack addNBTData(@NotNull final ItemStack stack, @NotNull final UUID uuid) {

    final ItemMeta meta = stack.getItemMeta();

    final PersistentDataContainer container = meta.getPersistentDataContainer();
    container.set(NamespacedKeyProvider.OWNER_UUID, PersistentDataType.STRING, uuid.toString());

    stack.setItemMeta(meta);

    return stack;
  }

  public static boolean isValidPlaceableHead(@NotNull final ItemStack stack) {
    final ItemMeta meta = stack.getItemMeta();
    final PersistentDataContainer container = meta.getPersistentDataContainer();
    return container.has(NamespacedKeyProvider.OWNER_UUID, PersistentDataType.INTEGER);
  }
}
