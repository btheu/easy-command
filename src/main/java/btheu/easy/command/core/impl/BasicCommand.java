package btheu.easy.command.core.impl;

import btheu.easy.command.bean.Command;

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
