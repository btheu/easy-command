package easycommand.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CommandList {

    public List<CommandState> commands = new ArrayList<>();

    @Data
    public static class CommandState {

        public String command;
        public String state;

    }
}
