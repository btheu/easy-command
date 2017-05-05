package easycommand.event;

import lombok.Data;

@Data
public class FinishEvent implements CommandEvent {

    private String message;

}
