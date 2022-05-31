package io.github.pulsebeat02.lifesteal.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public final class FileUtils {

  private FileUtils() {}

  public static boolean createFileIfNotExists(@NotNull final Path file) throws IOException {
    if (Files.notExists(file)) {
      Files.createFile(file);
      return true;
    }
    return false;
  }

}
