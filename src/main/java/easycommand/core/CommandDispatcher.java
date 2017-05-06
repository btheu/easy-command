package easycommand.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import easycommand.event.AddedEvent;
import easycommand.event.FinishedEvent;
import easycommand.event.ProgressEvent;
import easycommand.event.StartedEvent;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

@Slf4j
@Listener
public class CommandDispatcher {

    // user who want to know all events
    public static List<Session> usersAll = Collections.synchronizedList(new ArrayList<>());

    // user who want to know the list of command
    public static List<Session> usersList = Collections.synchronizedList(new ArrayList<>());

    // user who want to know events of a specific command
    public static Map<Session, String> usersByCommand = new ConcurrentHashMap<>();

    // key: command
    public static Map<String, String> commandByState = new ConcurrentHashMap<>();

    private void closeAndRemoveConsult(String command) {
        List<Entry<Session, String>> collect = usersByCommand.entrySet().stream().peek(e -> {
            log.info("{} =? {}", command, e.getValue());
        }).filter(e -> {
            return e.getValue().equalsIgnoreCase(command);
        }).map(entry -> {
            return entry;
        }).collect(Collectors.toList());

        collect.forEach(entry -> {
            try {
                JSONObject response = new JSONObject();
                response.put("message", "Disconnecting");

                String valueOf = String.valueOf(response);
                entry.getKey().getRemote().sendString(valueOf);
            } catch (Exception e) {
                e.printStackTrace();
            }

            entry.getKey().close();

            usersByCommand.remove(entry.getKey(), entry.getValue());
        });

    }

    private void broadcastMessageToConsult(String command, String message) {
        usersByCommand.entrySet().stream().peek(e -> {
            log.info("{} =? {}", command, e.getValue());
        }).filter(e -> {
            return e.getValue().equalsIgnoreCase(command);
        }).forEach(entry -> {
            try {
                JSONObject response = new JSONObject();
                response.put("message", message);

                String valueOf = String.valueOf(response);
                entry.getKey().getRemote().sendString(valueOf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void broadcastMessageToAll(String message) {
        usersAll.stream().filter(Session::isOpen).forEach(session -> {
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

    public static void broadcastMessageToList() {
        List<JSONObject> commands = commandByState.entrySet().stream().map(e -> {

            JSONObject command = new JSONObject();
            command.put("command", e.getKey());
            command.put("state", e.getValue());
            return command;
        }).collect(Collectors.toList());

        JSONArray array = new JSONArray(commands);

        JSONObject response = new JSONObject();
        response.put("commands", array);

        String valueOf = String.valueOf(response);

        usersList.stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(valueOf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateState(String command, String state) {

        String oldState = commandByState.get(command);
        if (oldState == null || !oldState.equalsIgnoreCase(state)) {
            commandByState.put(command, state);

            broadcastMessageToList();
        }
    }

    @Handler
    public void handle(AddedEvent event) {
        updateState(event.getCommand(), "added");

        log.debug("{} ", event.getMessage());
        broadcastMessageToAll(event.getMessage());

        broadcastMessageToConsult(event.getCommand(), event.getMessage());
    }

    @Handler
    public void handle(StartedEvent event) {
        updateState(event.getCommand(), "running");

        log.debug("{} ", event.getMessage());
        broadcastMessageToAll(event.getMessage());

        broadcastMessageToConsult(event.getCommand(), event.getMessage());
    }

    @Handler
    public void handle(ProgressEvent event) {
        // updateState(event.getCommand(), "running");

        log.debug("{} ", event.getMessage());
        broadcastMessageToAll(event.getMessage());

        broadcastMessageToConsult(event.getCommand(), event.getMessage());
    }

    @Handler
    public void handle(FinishedEvent event) {
        updateState(event.getCommand(), "finished");

        log.debug("{} ", event.getMessage());
        broadcastMessageToAll(event.getMessage());

        broadcastMessageToConsult(event.getCommand(), event.getMessage());

        closeAndRemoveConsult(event.getCommand());
        // TODO close specific user
    }

}
