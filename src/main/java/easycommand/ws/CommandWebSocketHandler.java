package easycommand.ws;

import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import easycommand.core.CommandDispatch;

@WebSocket
public class CommandWebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {

        Map<String, List<String>> params = user.getUpgradeRequest().getParameterMap();

        if (params != null && params.containsKey("command")) {
            CommandDispatch.usersByCommand.put(user, params.get("command").get(0));
        } else {
            CommandDispatch.usersAll.add(user);
        }

    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        CommandDispatch.usersAll.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {

    }

}