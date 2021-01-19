package cal.codes.larynx.dialoge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

public class UsualDialogue extends Dialogue {
  public Codec<UsualDialogue> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.STRING.fieldOf("text").forGetter(o -> o.text),
                      Codec.STRING.fieldOf("author").forGetter(o -> o.author),
                      Codec.STRING.fieldOf("color").forGetter(o -> o.color),
                      Codec.BOOL.fieldOf("translateText").forGetter(o -> o.translateText),
                      Codec.BOOL.fieldOf("translateAuthor").forGetter(o -> o.translateAuthor))
                  .apply(instance, UsualDialogue::new));

  public UsualDialogue(
      String text,
      String author,
      String color,
      @Nullable boolean translateText,
      @Nullable boolean translateAuthor) {
    super(text, author, color, translateText, translateAuthor);
  }
}
