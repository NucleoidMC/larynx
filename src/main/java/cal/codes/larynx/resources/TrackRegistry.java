package cal.codes.larynx.resources;

import cal.codes.larynx.tracks.DialogueTrack;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.Plasmid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;

public class TrackRegistry {
    public static final HashMap<Identifier, DialogueTrack> INSTANCE = new HashMap<>();

    public static void init() {
        ResourceManagerHelper serverData = ResourceManagerHelper.get( ResourceType.SERVER_DATA);
        serverData.registerReloadListener( new TrackRegistryReloader()  );
    }

    protected static class TrackRegistryReloader implements SimpleSynchronousResourceReloadListener {

        @Override
        public Identifier getFabricId() {
            return new Identifier("larynx", "dialog_tracks");
        }

        @Override
        public void apply(ResourceManager manager) {
            registry.clear();
            Collection<Identifier> resources = manager.findResources("dialog_tracks", path -> path.endsWith(".json"));

            for (Identifier path : resources) {
                try {
                    Resource resource = manager.getResource(path);
                    try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                        JsonElement json = new JsonParser().parse(reader);
                        DataResult<DialogueTrack> data = DialogueTrack.CODEC.decode( JsonOps.INSTANCE, json).map(Pair::getFirst);
                        data.result().ifPresent( dialogueTrack ->  {
                            registry.put(dialogueTrack.id, dialogueTrack);
                        });
                        data.error().ifPresent(error -> {
                            Plasmid.LOGGER.error("[Larynx] Failed to decode dialog track at {}: {}", path, error.toString());
                        });
                    }
                } catch (IOException e) {
                    Plasmid.LOGGER.error("[Larynx] Failed to read dialog track at {}", path, e);
                }
            }

        }
    }

}

