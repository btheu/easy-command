package easycommand.core;

import easycommand.bean.Command;

public interface CommandRunnable extends Runnable {

    public void prepare(Command command);

}
