package easycommand.chat;

import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

public class Chat {

    // this map is shared between sessions and threads, so it needs to be
    // thread-safe (http://stackoverflow.com/a/2688817)
    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();

    // Used for creating the next username
    static int nextUserNumber = 1;

    // Sends a message from one user to all users, along with a list of current
    // usernames
    public static void broadcastMessage(String sender, String message) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                JSONObject response = new JSONObject();
                response.put("userMessage", createHtmlMessageFromSender(sender, message));
                response.put("userlist", userUsernameMap.values());

                String valueOf = String.valueOf(response);
                session.getRemote().sendString(valueOf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article()
                .with(b(sender + " says:"), p(message),
                        span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date())))
                .render();
    }
}