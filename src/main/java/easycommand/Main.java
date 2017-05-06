package easycommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import easycommand.chat.ChatWebSocketHandler;
import easycommand.core.Command;
import easycommand.core.CommandDispatch;
import easycommand.core.CommandExecutor;
import easycommand.core.impl.BasicCommand;
import easycommand.core.impl.DefaultCommandExecutor;
import easycommand.event.EventBus;
import easycommand.ws.CommandListWebSocketHandler;
import easycommand.ws.CommandWebSocketHandler;
import spark.Spark;

public class Main {

    public static CommandDispatch collector = new CommandDispatch();

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

            Command command = new BasicCommand(commandString);

            executor.submit(command);

            return "ok";
        });

        Spark.get("/hello", (req, res) -> {

            Thread.sleep(10_000);

            return "Command executed !!";
        });

        Spark.init();
    }

    public static void run() {
        String command = "netstat";
        try {
            Process process = Runtime.getRuntime().exec(command);
            System.out.println("the output stream is " + process.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = reader.readLine()) != null) {
                System.out.println("The inout stream is " + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
