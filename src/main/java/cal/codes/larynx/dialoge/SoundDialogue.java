package cal.codes.larynx.dialoge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.plasmid.game.player.PlayerSet;

public class SoundDialogue extends Dialogue<SoundDialogue> {
  public SoundEvent sound;
  private String soundID;
  public Codec<SoundDialogue> CODEC =
      RecordCodecBuilder.create(
          instance ->
              instance
                  .group(
                      Codec.STRING.fieldOf("sound").forGetter(o -> o.soundID),
                      Codec.STRING.fieldOf("text").forGetter(o -> o.text),
                      Codec.STRING.fieldOf("author").forGetter(o -> o.author),
                      Codec.STRING.fieldOf("color").forGetter(o -> o.color),
                      Codec.BOOL.fieldOf("translateText").forGetter(o -> o.translateText),
                      Codec.BOOL.fieldOf("translateAuthor").forGetter(o -> o.translateAuthor))
                  .apply(instance, SoundDialogue::new));

  public SoundDialogue(
      String sound,
      String text,
      String author,
      String color,
      @Nullable boolean translateText,
      @Nullable boolean translateAuthor) {
    super(text, author, color, translateText, translateAuthor);
    this.sound = Registry.SOUND_EVENT.get(Identifier.tryParse(sound));
  }

  @Override
  public Codec<SoundDialogue> getCODEC() {
    return CODEC;
  }

  @Override
  public void setCODEC(Codec<SoundDialogue> CODEC) {
    this.CODEC = CODEC;
  }

  @Override
  public void Play(PlayerSet players) {
    players.sendSound(sound, SoundCategory.PLAYERS, 1f, 1f);
    super.Play(players);
  }
}
