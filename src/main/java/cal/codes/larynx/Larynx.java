package cal.codes.larynx;

import cal.codes.larynx.dialoge.UsualDialogue;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;

public class Larynx implements DedicatedServerModInitializer {

  public final boolean DEV = FabricLoader.getInstance().isDevelopmentEnvironment();

  @Override
  public void onInitializeServer() {
      System.out.println("Test");
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, dedicated) -> {
          dispatcher.register(
              CommandManager.literal("larynxtest")
                  .executes(
                      context -> {
                        UsualDialogue dialogue =
                            new UsualDialogue("Hello!", "Bob", "Aqua", false, false);
                        dialogue.Play(context.getSource().getPlayer());
                        return 1;
                      }));
        });
  }
}
