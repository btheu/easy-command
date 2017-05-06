package easycommand.ws;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import easycommand.core.CommandDispatch;

@WebSocket
public class CommandListWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        CommandDispatch.usersList.add(user);
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        CommandDispatch.usersList.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {

    }

}