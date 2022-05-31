package io.github.pulsebeat02.lifesteal.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import io.github.pulsebeat02.lifesteal.gson.adapter.UUIDAdapter;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class GsonProvider {

  private static final Gson GSON;

  static {
    GSON = getBuilder().create();
  }

  private static @NotNull GsonBuilder getBuilder() {

    final GsonBuilder builder = new GsonBuilder();
    getAdapters().forEach(builder::registerTypeAdapter);

    return builder;
  }

  @Contract(" -> new")
  private static @NotNull @Unmodifiable Map<Class<?>, TypeAdapter<?>> getAdapters() {
    return Map.of(UUID.class, new UUIDAdapter());
  }

  public static @NotNull Gson getGson() {
    return GSON;
  }
}
