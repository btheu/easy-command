package btheu.easy.command.event;

import lombok.Data;

@Data
public class ProgressEvent implements CommandEvent {

    private String command;

    private String message;

}