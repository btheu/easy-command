package btheu.easy.command.event;

import lombok.Data;

@Data
public class StartedEvent implements CommandEvent {

    private String command;

    private String message;

}