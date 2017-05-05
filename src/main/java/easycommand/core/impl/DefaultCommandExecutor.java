package easycommand.core.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import easycommand.core.Command;
import easycommand.core.CommandExecutor;
import easycommand.core.CommandRunnable;

public class DefaultCommandExecutor implements CommandExecutor {

    protected static ExecutorService executor = Executors.newFixedThreadPool(2);

    @Override
    public void submit(Command command) {

        CommandRunnable runnable = new SimpleCommandRunnable();
        runnable.prepare(command);

        executor.execute(runnable);

    }

    @Override
    public void waitForEnd() {
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
