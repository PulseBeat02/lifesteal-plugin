package io.github.pulsebeat02.lifesteal.utils;

import io.github.pulsebeat02.lifesteal.Lifesteal;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.jetbrains.annotations.NotNull;

public final class TaskUtils {

  private TaskUtils() {}

  public static <T> @NotNull Future<T> sync(
      @NotNull final Lifesteal lifesteal, @NotNull final Callable<T> task) {
    return lifesteal.getServer().getScheduler().callSyncMethod(lifesteal, task);
  }
}
