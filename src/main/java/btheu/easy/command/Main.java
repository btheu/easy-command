package btheu.easy.command;

import btheu.easy.command.bean.Command;
import btheu.easy.command.bean.CommandList;
import btheu.easy.command.chat.ChatWebSocketHandler;
import btheu.easy.command.core.CommandDispatcher;
import btheu.easy.command.core.CommandExecutor;
import btheu.easy.command.core.impl.DefaultCommandExecutor;
import btheu.easy.command.event.EventBus;
import btheu.easy.command.utils.CliUtils;
import btheu.easy.command.utils.GsonTransformer;
import btheu.easy.command.ws.CommandListWebSocketHandler;
import btheu.easy.command.ws.CommandWebSocketHandler;
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

            Command command = new Command(CliUtils.parse(commandString));

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
