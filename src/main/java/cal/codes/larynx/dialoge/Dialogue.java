package cal.codes.larynx.dialoge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.plasmid.game.player.PlayerSet;

import java.util.Objects;

public abstract class Dialogue {
  public String text;
  public String author;
  public String color;
  public boolean translateText = false;
  public boolean translateAuthor = false;

  public Dialogue(
      String text,
      String author,
      String color,
      @Nullable boolean translateText,
      @Nullable boolean translateAuthor) {
    this.text = text;
    this.author = author;
    this.color = color;

    if (translateText) this.translateText = translateText;
    if (translateAuthor) this.translateText = translateText;
  }

  /**
   * If the text should be parsed into a TranslatableText or LiteralText
   *
   * @return boolean
   */
  public boolean isTranslateText() {
    return translateText;
  }

  /**
   * Mark the text as translatable or non-translatable
   *
   * @param translate
   */
  public void setTranslateText(boolean translate) {
    translateText = translate;
  }

  /**
   * If the author should be parsed into a TranslatableText or LiteralText
   *
   * @return boolean
   */
  public boolean isTranslateAuthor() {
    return translateAuthor;
  }

  /** Get the text. */
  public String getText() {
    return text;
  }

  /**
   * Set the text.
   *
   * @param text The text as a string.
   */
  public void setText(String text) {
    this.text = text;
  }

  /** Get the author. */
  public String getAuthor() {
    return author;
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, author, color);
  }

  public void play(ServerPlayerEntity player) {
    String message = "";
    String author = "";
    if (translateText) {
      message = new TranslatableText(text).getKey();
    } else {
      message = text;
    }
    if (translateAuthor) {
      author = new TranslatableText(author).getKey();
    } else {
      author = this.author;
    }
    MutableText authorText =
        new LiteralText("<")
            .formatted(Formatting.WHITE)
            .append(new LiteralText(author).formatted(Formatting.valueOf(color.toUpperCase())))
            .append(new LiteralText("> ").formatted(Formatting.WHITE));
    player.sendMessage(
        authorText.append(new LiteralText(message).formatted(Formatting.WHITE)), false);
  }

  public void play(PlayerSet players) {
    String message = "";
    String author = "";
    if (translateText) {
      message = new TranslatableText(text).getKey();
    } else {
      message = text;
    }
    if (translateAuthor) {
      author = new TranslatableText(author).getKey();
    } else {
      author = this.author;
    }
    MutableText authorText =
        new LiteralText("<")
            .formatted(Formatting.WHITE)
            .append(new LiteralText(author).formatted(Formatting.valueOf(color.toUpperCase())))
            .append(new LiteralText("> ").formatted(Formatting.WHITE));
    players.sendMessage(authorText.append(new LiteralText(message).formatted(Formatting.WHITE)));
  }
}