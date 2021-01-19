package cal.codes.larynx;

import cal.codes.larynx.tracks.DialogueTrack;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Random;

public class Larynx implements DedicatedServerModInitializer {
  public final boolean DEV = FabricLoader.getInstance().isDevelopmentEnvironment();
  /**
   * Get a file from the resources folder of a jar.
   *
   * @param path The path of where the file is.
   * @param root Any class from the jar you want to get resources from.
   * @return A file class you can use.
   * @throws Exception
   */
  public static InputStream getFileFromResource(String path, Class root) throws Exception {
    URL resource = root.getClassLoader().getResource(path);
    System.out.println(resource);
    if (resource == null) {
      throw new IllegalArgumentException("file not found! " + path);
    } else {
      InputStream in = root.getClassLoader().getResourceAsStream(path);
      return in;
    }
  }
  /**
   * Get a DialogueTrack from your mod.
   *
   * @param ID The ID of the file, eg: mymod:mytrack == "/assets/mymod/dialogues/mytrack.json"
   * @param classRef Any class from your mod.
   * @return DialogueTrack
   */
  public static DialogueTrack get(Identifier ID, Class classRef) {
    Optional<DialogueTrack> ef = null;
    try {
      InputStream file = getFileFromResource(
              "assets/" + ID.getNamespace() + "/dialogues/" + ID.getPath() + ".json", classRef);
      JsonParser e = new JsonParser();
        JsonElement element = e.parse(new InputStreamReader(file));
        DataResult<Pair<DialogueTrack, JsonElement>> ee =
            DialogueTrack.CODEC.decode(JsonOps.INSTANCE, element);
        ef = ee.result().map(Pair::getFirst);
      } catch (Exception exception) {
      exception.printStackTrace();
    }
    return ef.get();
  }

  @Override
  public void onInitializeServer() {
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, dedicated) -> {
          if (!DEV) return;
          dispatcher.register(
              CommandManager.literal("larynxtest")
                  .executes(
                      context -> {
                        get(new Identifier("larynx", "mytrack"), Larynx.class)
                            .play(context.getSource().getPlayer());
                        return 1;
                      }));
        });
  }
}
