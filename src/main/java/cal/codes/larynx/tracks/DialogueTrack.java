package cal.codes.larynx.tracks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import xyz.nucleoid.plasmid.game.player.PlayerSet;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DialogueTrack {
  public static Codec<DialogueTrack> CODEC =
      RecordCodecBuilder.create(
          dialogueTrackInstance ->
              dialogueTrackInstance
                  .group(
                      DialogueLine.CODEC.listOf().fieldOf("lines").forGetter( e -> e.dialogues),
                      Codec.INT.fieldOf("ticksBetween").forGetter(e -> e.ticksBetween))
                  .apply(dialogueTrackInstance, DialogueTrack::new));
  public List<DialogueLine> dialogues;
  public int ticksBetween;

  public DialogueTrack(List<DialogueLine> dialogues, int ticksBetween) {
    this.dialogues = dialogues;
    this.ticksBetween = ticksBetween;
  }

  /**
   * Play the track to a playerset.
   *
   * @param players
   */
  public void play(PlayerSet players) {
    int currentDelay = 0;
    Timer timer = new Timer();
    for (DialogueLine entry : this.dialogues) {
      timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              entry.getDialogue().play(players);
            }
          },
          (long) (currentDelay) / 20 * 1000);
      if (entry.delay == -1) {
        currentDelay += this.ticksBetween;
      } else {
        currentDelay += entry.delay;
      }
    }
  }

  /**
   * Play the track to a single player.
   *
   * @param player
   */
  public void play(ServerPlayerEntity player) {
    int currentDelay = 0;
    Timer timer = new Timer();
    for (DialogueLine entry : this.dialogues) {
      timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              entry.getDialogue().play(player);
            }
          },
          (long) (currentDelay) / 20 * 1000);
      if (entry.delay == -1) {
        currentDelay += this.ticksBetween;
      } else {
        currentDelay += entry.delay;
      }
    }
  }
}
