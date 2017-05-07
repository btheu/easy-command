package easycommand.event;

import lombok.Data;

@Data
public class KillEvent implements CommandEvent {

    private String commandId;

    public KillEvent(String commandId) {
        this.commandId = commandId;
    }

}
