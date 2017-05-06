package easycommand.event;

import net.engio.mbassy.bus.MBassador;

public class EventBus {

    protected static MBassador<CommandEvent> bus = new MBassador<CommandEvent>();

    public static void subscribe(Object listener) {
        bus.subscribe(listener);
    }

    public static void post(CommandEvent event) {
        bus.post(event).now();
    }

}
