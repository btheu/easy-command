package easycommand.core.impl;

import easycommand.core.Command;

public class BasicCommand implements Command {

    private String[] command;

    public BasicCommand(String... command) {
        this.command = command;
    }

    @Override
    public String[] command() {
        return command;
    }

}
