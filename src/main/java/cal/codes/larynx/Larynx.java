package cal.codes.larynx;

import cal.codes.larynx.tracks.DialogueTrack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

public class Larynx implements DedicatedServerModInitializer {

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final JsonParser JSON_PARSER = new JsonParser();

  public final boolean DEV = FabricLoader.getInstance().isDevelopmentEnvironment();

  /**
   * Get a file from the resources folder of a jar.
   *
   * @param path The path of where the file is.
   * @param root Any class from the jar you want to get resources from.
   * @return A file class you can use.
   * @throws Exception
   */
  public static File getFileFromResource(String path, Class root) throws Exception {
    URL resource = root.getClassLoader().getResource(path);
    System.out.println(resource);
    if (resource == null) {
      throw new IllegalArgumentException("file not found! " + path);
    } else {
      File tmp = File.createTempFile(String.valueOf(new Random().nextLong()), "cache-larynx.json");
      InputStream in = root.getClassLoader().getResourceAsStream(path);
      try (OutputStream outputStream = new FileOutputStream(tmp)) {
        IOUtils.copy(in, outputStream);
      } catch (IOException e) {
        throw e;
      }
      if (tmp == null) {
        throw new Exception("File is null.");
      }
      return tmp;
    }
  }

  private static String readFile(String path, Charset encoding) throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
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
      File file =
          getFileFromResource(
              "assets/" + ID.getNamespace() + "/dialogues/" + ID.getPath() + ".json", classRef);
      JsonParser e = new JsonParser();
      try (InputStream input = Files.newInputStream(file.toPath())) {
        JsonElement element = e.parse(new InputStreamReader(input));
        DataResult<Pair<DialogueTrack, JsonElement>> ee = DialogueTrack.CODEC.decode(JsonOps.INSTANCE, element);
        ef = ee.result().map(Pair::getFirst);
      }
    } catch (Exception e) {
      e.printStackTrace();
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
