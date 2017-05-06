package easycommand.core;

public interface CommandExecutor {

    public void submit(Command command);

    /**
     * Useless in server context
     */
    public void waitForEnd();

}
