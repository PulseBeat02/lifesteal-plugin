package io.github.pulsebeat02.lifesteal.gson;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

public final class GsonProvider {

  private static final Gson GSON;

  static {
    GSON = new Gson();
  }

  public static @NotNull Gson getGson() {
    return GSON;
  }
}
