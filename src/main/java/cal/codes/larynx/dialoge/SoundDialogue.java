package cal.codes.larynx.dialoge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.plasmid.game.player.PlayerSet;

public class SoundDialogue extends Dialogue {
  public SoundEvent sound;

  public SoundDialogue(
      SoundEvent sound,
      String text,
      String author,
      String color,
      @Nullable boolean translateText,
      @Nullable boolean translateAuthor) {
    super(text, author, color, translateText, translateAuthor);
    this.sound = sound;
  }

  @Override
  public void play(PlayerSet players) {
    players.sendSound(sound, SoundCategory.PLAYERS, 1f, 1f);
    super.play(players);
  }

  @Override
  public void play(ServerPlayerEntity player) {
    player.playSound(sound, SoundCategory.PLAYERS, 1f, 1f);
    super.play(player);
  }
}
