package io.vertx.example.shell.echokeyboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.TelnetTermOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EchoKeyboardCommand extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(EchoKeyboardCommand.class);
  }

  @Override
  public void start() throws Exception {

    Command starwars = CommandBuilder.command("echokeyboard").
        processHandler(process -> {

          // Echo
          process.stdinHandler(keys -> {
            process.write(keys.replace('\r', '\n'));
          });

          // Terminate when user hits Ctrl-C
          process.interruptHandler(v -> {
            process.end();
          });

        }).build(vertx);

    ShellService service = ShellService.create(vertx, new ShellServiceOptions().setTelnetOptions(
        new TelnetTermOptions().setHost("localhost").setPort(3000)
    ));
    CommandRegistry.getShared(vertx).registerCommand(starwars);
    service.start(ar -> {
      if (!ar.succeeded()) {
        ar.cause().printStackTrace();
      }
    });
  }
}
