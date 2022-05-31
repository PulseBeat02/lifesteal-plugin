package io.github.pulsebeat02.lifesteal.gson.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class UUIDAdapter extends TypeAdapter<UUID> {

  @Override
  public void write(@NotNull final JsonWriter out, @NotNull final UUID value) throws IOException {
    out.beginObject();
    out.name("uuid").value(value.toString());
    out.endObject();
  }

  @Override
  public @NotNull UUID read(@NotNull final JsonReader in) throws IOException {
    in.beginObject();
    final String id = this.readUUID(in);
    in.close();
    return UUID.fromString(id);
  }

  private @NotNull String readUUID(@NotNull final JsonReader in) throws IOException {
    while (in.hasNext()) {
      if (this.isUUIDKey(in)) {
        return in.nextString();
      }
    }
    throw new AssertionError("Could not find uuid value of JSON configuration!");
  }

  private boolean isUUIDKey(@NotNull final JsonReader in) throws IOException {
    return in.nextName().equals("uuid");
  }
}
