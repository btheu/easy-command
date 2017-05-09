package btheu.easy.command.bean;

import java.util.Arrays;
import java.util.Base64;

import lombok.Data;

@Data
public class Command {

    public String id;

    public String[] commands;

    public Command(String... commands) {
        this.commands = commands;
        this.id = Base64.getEncoder().encodeToString(Arrays.toString(this.commands).getBytes());
    }

}
