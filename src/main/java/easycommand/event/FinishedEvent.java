package easycommand.event;

import lombok.Data;

@Data
public class FinishedEvent implements CommandEvent {

    private String command;

    private String message;

}