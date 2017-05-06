package easycommand.ws;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import easycommand.core.CommandDispatcher;

@WebSocket
public class CommandListWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        CommandDispatcher.usersList.add(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        CommandDispatcher.usersList.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {

    }

}