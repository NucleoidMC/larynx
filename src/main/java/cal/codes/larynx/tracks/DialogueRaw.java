package cal.codes.larynx.tracks;

import cal.codes.larynx.dialoge.Dialogue;
import cal.codes.larynx.dialoge.SoundDialogue;
import cal.codes.larynx.dialoge.UsualDialogue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DialogueRaw {
  public static final Codec<DialogueRaw> CODEC =
      RecordCodecBuilder.create(
          objectInstance ->
              objectInstance
                  .group(
                      Codec.STRING.fieldOf("type").forGetter(o -> o.type),
                      Codec.STRING.fieldOf("text").forGetter(o -> o.text),
                      Codec.STRING.fieldOf("author").forGetter(o -> o.author),
                      Codec.STRING.fieldOf("color").forGetter(o -> o.color),
                      Codec.STRING.optionalFieldOf("sound").forGetter(o -> Optional.of(o.soundID)),
                      Codec.INT.optionalFieldOf("delay").forGetter(o -> Optional.of(o.delay)),
                      Codec.BOOL
                          .optionalFieldOf("translateText")
                          .forGetter(o -> Optional.of(o.translateText)),
                      Codec.BOOL
                          .optionalFieldOf("translateAuthor")
                          .forGetter(o -> Optional.of(o.translateAuthor)))
                  .apply(
                      objectInstance,
                      (type, text, author, color, sound, delay, tranText, tranAuth) -> {
                        SoundEvent soundd = null;
                        boolean tranTextt = false;
                        boolean tranAuthh = false;
                        int delayy = -1;

                        if (delay.isPresent()) delayy = delay.get();
                        if (tranAuth.isPresent()) tranAuthh = tranAuth.get();
                        if (tranText.isPresent()) tranTextt = tranText.get();
                        if (sound.isPresent())
                          soundd = Registry.SOUND_EVENT.get(Identifier.tryParse(sound.get()));

                        return new DialogueRaw(
                            type, text, author, color, soundd, tranTextt, tranAuthh, delayy);
                      }));
  public String type;
  public String text;
  public String author;
  public String color;
  @Nullable public SoundEvent sound;
  public boolean translateText = false;
  public boolean translateAuthor = false;
  @Nullable public String soundID;
  public int delay;

  public DialogueRaw(
      String type,
      String text,
      String author,
      String color,
      @Nullable SoundEvent sound,
      @Nullable boolean translateText,
      @Nullable boolean translateAuthor,
      @Nullable int delay) {
    this.type = type;
    this.text = text;
    this.author = author;
    this.color = color;
    this.sound = sound;
    this.translateText = translateText;
    this.translateAuthor = translateAuthor;
    this.delay = delay;
  }

  public @Nullable Dialogue getDialogue() {
    Dialogue d;
    switch (type) {
      case "usual":
        d = new UsualDialogue(text, author, color, translateText, translateAuthor);
        break;
      case "sound":
        d = new SoundDialogue(sound, text, author, color, translateText, translateAuthor);
        break;
      default:
        d = new UsualDialogue(text, author, color, translateText, translateAuthor);
        break;
    }
    return d;
  }
}
