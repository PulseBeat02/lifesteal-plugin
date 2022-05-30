package io.github.pulsebeat02.lifesteal.locale;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.separator;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Locale {

  UniComponent<Sender, String> REQUIRED_ARGUMENT =
      (argument) ->
          text()
              .color(DARK_GRAY)
              .append(text("<"))
              .append(text(argument, GRAY))
              .append(text(">"))
              .build();

  UniComponent<Sender, String> OPTIONAL_ARGUMENT =
      (argument) ->
          text()
              .color(DARK_GRAY)
              .append(text("["))
              .append(text(argument, GRAY))
              .append(text("]"))
              .build();

  NullComponent<Sender> PLAYER_ZERO_HEARTS =
      () ->
          format(
              text(
                  "You have died to a player and lost all of your "
                      + "hearts! Your head has been dropped. A fellow "
                      + "teammate can place the head and sacrifice one of "
                      + "their hearts to revive you!",
                  RED));

  NullComponent<Sender> PLAYER_LOSE_HEART =
      () -> format(text("You have died or sacrificed a heart to a player and lost a heart!", RED));

  NullComponent<Sender> PLAYER_GAIN_HEART =
      () ->
          format(text("You have killed or have been revived by a player and gained a heart!", RED));

  NullComponent<Sender> SMP_REVIVE_GUI_TITLE = () -> text("Revive Player", GREEN, BOLD);

  NullComponent<Sender> SMP_REVIVE_GUI_CONFIRM_NAME = () -> text("Confirm", GREEN, BOLD);

  NullComponent<Sender> SMP_REVIVE_GUI_CONFIRM_LORE = () -> text("Sacrifice a heart to revive your teammate", GRAY);

  NullComponent<Sender> SMP_REVIVE_GUI_CANCEL_NAME = () -> text("Cancel", RED, BOLD);

  NullComponent<Sender> SMP_REVIVE_GUI_CANCEL_LORE = () -> text("Click to cancel", GRAY);

  @FunctionalInterface
  interface NullComponent<S extends Sender> {

    Component build();

    default void send(@NotNull final S sender) {
      sender.sendMessage(format(this.build()));
    }
  }

  @FunctionalInterface
  interface UniComponent<S extends Sender, A0> {

    Component build(A0 arg0);

    default void send(@NotNull final S sender, final A0 arg0) {
      sender.sendMessage(format(this.build(arg0)));
    }
  }

  @FunctionalInterface
  interface BiComponent<S extends Sender, A0, A1> {

    Component build(A0 arg0, A1 arg1);

    default void send(@NotNull final S sender, @NotNull final A0 arg0, @NotNull final A1 arg1) {
      sender.sendMessage(format(this.build(arg0, arg1)));
    }
  }

  @FunctionalInterface
  interface TriComponent<S extends Sender, A0, A1, A2> {

    Component build(A0 arg0, A1 arg1, A2 arg2);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2)));
    }
  }

  @FunctionalInterface
  interface QuadComponent<S extends Sender, A0, A1, A2, A3> {

    Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2,
        @NotNull final A3 arg3) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2, arg3)));
    }
  }

  @FunctionalInterface
  interface PentaComponent<S extends Sender, A0, A1, A2, A3, A4> {

    Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2,
        @NotNull final A3 arg3,
        @NotNull final A4 arg4) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2, arg3, arg4)));
    }
  }

  @FunctionalInterface
  interface HexaComponent<S extends Sender, A0, A1, A2, A3, A4, A5> {

    Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);

    default void send(
        @NotNull final S sender,
        @NotNull final A0 arg0,
        @NotNull final A1 arg1,
        @NotNull final A2 arg2,
        @NotNull final A3 arg3,
        @NotNull final A4 arg4,
        @NotNull final A5 arg5) {
      sender.sendMessage(format(this.build(arg0, arg1, arg2, arg3, arg4, arg5)));
    }
  }

  NullComponent<Sender> PLUGIN_PREFIX =
      () ->
          text()
              .color(RED)
              .append(text('['), text("SMP", GREEN), text(']'), space(), text("»", GRAY))
              .build();

  static @NotNull Component format(@NotNull final Component message) {
    return join(separator(space()), PLUGIN_PREFIX.build(), message);
  }
}
