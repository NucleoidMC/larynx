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

public class DialogueLine {
  public static final Codec<DialogueLine> CODEC =
      RecordCodecBuilder.create(
          objectInstance ->
              objectInstance
                  .group(
                      Codec.STRING.fieldOf("type").forGetter(o -> o.type),
                      Codec.STRING.fieldOf("text").forGetter(o -> o.text),
                      Codec.STRING.fieldOf("author").forGetter(o -> o.author),
                      Codec.STRING.fieldOf("color").forGetter(o -> o.color),
                      Codec.STRING.optionalFieldOf("sound", "none").forGetter(o -> o.soundID),
                      Codec.INT.optionalFieldOf("delay", -1).forGetter(o -> o.delay),
                      Codec.BOOL
                          .optionalFieldOf("translateText", false)
                          .forGetter(o -> o.translateText),
                      Codec.BOOL
                          .optionalFieldOf("translateAuthor", false)
                          .forGetter(o -> o.translateAuthor))
                  .apply(
                      objectInstance,
                      (type, text, author, color, sound, delay, tranText, tranAuth) -> {
                        SoundEvent event;
                        switch (sound) {
                          case "none":
                            event = null;
                            break;
                          default:
                            event = Registry.SOUND_EVENT.get(Identifier.tryParse(sound));
                            break;
                        }

                        return new DialogueLine(
                            type, text, author, color, event, tranText, tranAuth, delay);
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

  public DialogueLine(
      String type,
      String text,
      String author,
      String color,
      @Nullable SoundEvent sound,
      boolean translateText,
      boolean translateAuthor,
      int delay) {
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
