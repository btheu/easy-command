package easycommand;

import spark.Spark;

public class Main {

    public static void main(String[] args) {

        Spark.staticFiles.location("/public");
        // Spark.staticFiles.externalLocation("C:/tmp/easy-command/");

        Spark.webSocket("/chat", ChatWebSocketHandler.class);

        Spark.get("/hello", (req, res) -> {

            Thread.sleep(10_000);

            return "Command executed !!";
        });

        Spark.init();
    }

}
