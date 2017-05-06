package easycommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import easycommand.chat.ChatWebSocketHandler;
import easycommand.core.Command;
import easycommand.core.CommandCollector;
import easycommand.core.CommandExecutor;
import easycommand.core.CommandWebSocketHandler;
import easycommand.core.impl.BasicCommand;
import easycommand.core.impl.DefaultCommandExecutor;
import easycommand.event.EventBus;
import spark.Spark;

public class Main {

    public static CommandCollector collector = new CommandCollector();

    public static CommandExecutor executor = new DefaultCommandExecutor();
    static {
        EventBus.subscribe(collector);
    }

    public static void main(String[] args) {

        Spark.staticFiles.location("/public");
        // Spark.staticFiles.externalLocation("C:/tmp/easy-command/");

        Spark.webSocket("/chat", ChatWebSocketHandler.class);

        Spark.webSocket("/command", CommandWebSocketHandler.class);

        Spark.post("/command", (req, res) -> {

            Command command = new BasicCommand("Via Post");

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
