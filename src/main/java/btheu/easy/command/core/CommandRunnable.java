package btheu.easy.command.core;

import btheu.easy.command.bean.Command;

public interface CommandRunnable extends Runnable {

    public void prepare(Command command);

}
