package cal.codes.larynx;

import cal.codes.larynx.resources.TrackRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;

public class Larynx implements DedicatedServerModInitializer {
  public final boolean DEV = FabricLoader.getInstance().isDevelopmentEnvironment();

  @Override
  public void onInitializeServer() {
    TrackRegistry.init();
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, dedicated) -> {
          if (!DEV) return;
          dispatcher.register(
              CommandManager.literal("larynxtest")
                  .executes(
                      context -> {
                        TrackRegistry.INSTANCE
                            .get(new Identifier("larynx", "mytrack"))
                            .play(context.getSource().getPlayer());
                        return 1;
                      }));
        });
  }
}
