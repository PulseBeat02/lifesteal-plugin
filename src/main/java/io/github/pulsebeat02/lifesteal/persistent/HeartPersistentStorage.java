package io.github.pulsebeat02.lifesteal.persistent;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.github.pulsebeat02.lifesteal.Lifesteal;
import io.github.pulsebeat02.lifesteal.gson.GsonProvider;
import io.github.pulsebeat02.lifesteal.hearts.HeartManager;
import io.github.pulsebeat02.lifesteal.utils.FileUtils;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import org.jetbrains.annotations.NotNull;

public final class HeartPersistentStorage {

  private final Lifesteal lifesteal;
  private final Gson gson;
  private final Path file;

  public HeartPersistentStorage(@NotNull final Lifesteal lifesteal) {
    this.lifesteal = lifesteal;
    this.gson = GsonProvider.getGson();
    this.file = lifesteal.getDataFolder().toPath().resolve("hearts.json");
  }

  public void read() throws IOException {
    try (final Reader reader = Files.newBufferedReader(this.file)) {
      final Runnable runnable = () -> this.parseAndModifyMap(reader);
      this.createFileAndOperate(runnable);
    }
  }

  @SuppressWarnings("UnstableApiUsage")
  private void parseAndModifyMap(@NotNull final Reader reader) {

    final Type type = new TypeToken<Map<UUID, Double>>() {}.getType();
    final Map<UUID, Double> map = this.gson.fromJson(reader, type);

    final HeartManager manager = this.lifesteal.getManager();
    map.forEach(manager::addExistingPlayer);
  }

  public void save() throws IOException {
    final Map<UUID, Double> map = this.lifesteal.getManager().getHeartsMap();
    try (final Writer writer = Files.newBufferedWriter(this.file)) {
      final Runnable runnable = () -> this.gson.toJson(map, writer);
      this.createFileAndOperate(runnable);
    }
  }

  private @NotNull CompletableFuture<Void> createFileAndOperate(@NotNull final Runnable runnable) {
    final ForkJoinPool pool = ForkJoinPool.commonPool();
    return CompletableFuture.runAsync(this::createFile, pool).thenRunAsync(runnable, pool);
  }

  private void createFile() {
    try {
      FileUtils.createFileIfNotExists(this.file);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
