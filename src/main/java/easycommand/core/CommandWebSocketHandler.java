package easycommand.core;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class CommandWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        CommandCollector.users.put(user, "");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        CommandCollector.users.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {

    }

}