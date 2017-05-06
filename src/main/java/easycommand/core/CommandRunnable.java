package easycommand.core;

public interface CommandRunnable extends Runnable {

    public void prepare(Command command);

}
