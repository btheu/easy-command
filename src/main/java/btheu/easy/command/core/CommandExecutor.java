package btheu.easy.command.core;

import btheu.easy.command.bean.Command;

public interface CommandExecutor {

    public void submit(Command command);

    /**
     * Useless in server context
     */
    public void waitForEnd();

    public void kill(String commandId);

}
