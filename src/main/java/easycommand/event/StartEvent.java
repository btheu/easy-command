package easycommand.event;

import lombok.Data;

@Data
public class StartEvent implements CommandEvent {

    private String message;

}
