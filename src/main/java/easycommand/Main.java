package easycommand;

import easycommand.chat.ChatWebSocketHandler;
import easycommand.core.Command;
import easycommand.core.CommandDispatcher;
import easycommand.core.CommandExecutor;
import easycommand.core.impl.BasicCommand;
import easycommand.core.impl.DefaultCommandExecutor;
import easycommand.event.EventBus;
import easycommand.utils.CliUtils;
import easycommand.ws.CommandListWebSocketHandler;
import easycommand.ws.CommandWebSocketHandler;
import spark.Spark;

public class Main {

    public static CommandDispatcher collector = new CommandDispatcher();

    public static CommandExecutor executor = new DefaultCommandExecutor();
    static {
        EventBus.subscribe(collector);
    }

    public static void main(String[] args) {
        Spark.staticFiles.location("/public");
        // Spark.staticFiles.externalLocation("C:/tmp/easy-command/");

        // 3 hours
        Spark.webSocketIdleTimeoutMillis(3 * 60 * 60 * 1_000);
        Spark.webSocket("/chat", ChatWebSocketHandler.class);

        Spark.webSocket("/command", CommandWebSocketHandler.class);

        Spark.webSocket("/command/list", CommandListWebSocketHandler.class);

        Spark.post("/command", (req, res) -> {

            String commandString = req.queryParams("command");

            Command command = new BasicCommand(CliUtils.parse(commandString));

            executor.submit(command);

            return "ok";
        });

        Spark.get("/hello", (req, res) -> {

            Thread.sleep(10_000);

            return "Command executed !!";
        });

        Spark.init();
    }
}
