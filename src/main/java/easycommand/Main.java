package easycommand;

import easycommand.bean.Command;
import easycommand.bean.CommandList;
import easycommand.chat.ChatWebSocketHandler;
import easycommand.core.CommandDispatcher;
import easycommand.core.CommandExecutor;
import easycommand.core.impl.BasicCommand;
import easycommand.core.impl.DefaultCommandExecutor;
import easycommand.event.EventBus;
import easycommand.utils.CliUtils;
import easycommand.utils.GsonTransformer;
import easycommand.ws.CommandListWebSocketHandler;
import easycommand.ws.CommandWebSocketHandler;
import spark.Spark;

public class Main {

    public static CommandDispatcher dispatcher = new CommandDispatcher();

    public static CommandExecutor executor = new DefaultCommandExecutor();
    static {
        EventBus.subscribe(dispatcher);
    }

    public static void main(String[] args) {
        Spark.staticFiles.location("/public");
        // Spark.staticFiles.externalLocation("C:/tmp/easy-command/");

        // 3 hours
        Spark.webSocketIdleTimeoutMillis(3 * 60 * 60 * 1_000);
        Spark.webSocket("/chat", ChatWebSocketHandler.class);

        Spark.webSocket("/command", CommandWebSocketHandler.class);

        Spark.webSocket("/command/list", CommandListWebSocketHandler.class);

        Spark.get("/command/list/", (req, res) -> {

            CommandList list = dispatcher.getCommandList();

            return list;
        }, new GsonTransformer());

        Spark.post("/command", (req, res) -> {

            String commandString = req.queryParams("command");

            Command command = new BasicCommand(CliUtils.parse(commandString));

            executor.submit(command);

            return "ok";
        });

        Spark.post("/command/kill", (req, res) -> {

            String commandString = req.queryParams("command");

            executor.kill(commandString);

            return "ok";
        });

        Spark.get("/hello", (req, res) -> {

            Thread.sleep(10_000);

            return "Command executed !!";
        });

        Spark.init();
    }
}
