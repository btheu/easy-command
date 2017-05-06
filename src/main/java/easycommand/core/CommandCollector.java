package easycommand.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import easycommand.event.AddedEvent;
import easycommand.event.FinishEvent;
import easycommand.event.ProgressEvent;
import easycommand.event.StartEvent;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

@Slf4j
@Listener
public class CommandCollector {

    public static Map<Session, String> users = new ConcurrentHashMap<>();

    public static void broadcastMessage(String message) {
        users.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                JSONObject response = new JSONObject();
                response.put("message", message);

                String valueOf = String.valueOf(response);
                session.getRemote().sendString(valueOf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Handler
    public void handle(AddedEvent event) {
        log.debug("{} ", event.getMessage());
        broadcastMessage(event.getMessage());
    }

    @Handler
    public void handle(StartEvent event) {
        log.debug("{} ", event.getMessage());
        broadcastMessage(event.getMessage());
    }

    @Handler
    public void handle(ProgressEvent event) {
        log.debug("{} ", event.getMessage());
        broadcastMessage(event.getMessage());
    }

    @Handler
    public void handle(FinishEvent event) {
        log.debug("{} ", event.getMessage());
        broadcastMessage(event.getMessage());
    }
}
