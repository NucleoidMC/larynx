package cal.codes.larynx.tracks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.game.player.PlayerSet;
import xyz.nucleoid.plasmid.util.Scheduler;

import java.util.List;

public class DialogueTrack {
  public static Codec<DialogueTrack> CODEC =
      RecordCodecBuilder.create(
          dialogueTrackInstance ->
              dialogueTrackInstance
                  .group(
                      DialogueLine.CODEC
                          .listOf()
                          .fieldOf("lines")
                          .forGetter((DialogueTrack e) -> e.dialogues),
                      Codec.INT
                          .fieldOf("defaultDelay")
                          .forGetter((DialogueTrack e) -> e.ticksBetween),
                      Codec.STRING.fieldOf("id").forGetter((DialogueTrack e) -> e.stringID))
                  .apply(dialogueTrackInstance, DialogueTrack::new));
  public List<DialogueLine> dialogues;
  public Identifier id;
  public int ticksBetween;
  private String stringID;

  public DialogueTrack(List<DialogueLine> dialogues, int ticksBetween, String id) {
    this.dialogues = dialogues;
    this.ticksBetween = ticksBetween;
    this.id = new Identifier(id);
  }

  /**
   * Play the track to a playerset.
   *
   * @param players
   */
  public void play(PlayerSet players) {
    int currentDelay = 0;
    for (DialogueLine entry : this.dialogues) {
      Scheduler.INSTANCE.submit(
          minecraftServer -> {
            entry.getDialogue().play(players);
          },
          currentDelay);
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
    for (DialogueLine entry : this.dialogues) {
      Scheduler.INSTANCE.submit(
          minecraftServer -> {
            entry.getDialogue().play(player);
          },
          currentDelay);
      if (entry.delay == -1) {
        currentDelay += this.ticksBetween;
      } else {
        currentDelay += entry.delay;
      }
    }
  }
}
