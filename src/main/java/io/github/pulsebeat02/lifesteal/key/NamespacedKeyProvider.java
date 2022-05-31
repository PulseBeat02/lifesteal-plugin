package io.github.pulsebeat02.lifesteal.key;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class NamespacedKeyProvider {

  public static final NamespacedKey OWNER_UUID;

  static {
    final Plugin plugin = JavaPlugin.getPlugin(Lifesteal.class);
    OWNER_UUID = new NamespacedKey(plugin, "owner-uuid");
  }
}
