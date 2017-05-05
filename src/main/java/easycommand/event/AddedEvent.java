package easycommand.event;

import lombok.Data;

@Data
public class AddedEvent implements CommandEvent {

    private String message;

}
