package easycommand.core.impl;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import easycommand.core.Command;
import easycommand.core.CommandExecutor;
import easycommand.core.CommandRunnable;
import easycommand.event.AddedEvent;
import easycommand.event.EventBus;

public class DefaultCommandExecutor implements CommandExecutor {

    protected static ExecutorService executor = Executors.newFixedThreadPool(2);

    @Override
    public void submit(Command command) {

        this.onSubmit(command);

        CommandRunnable runnable = new SimpleCommandRunnable();
        runnable.prepare(command);

        executor.execute(runnable);

    }

    protected void onSubmit(Command command) {
        String string = Arrays.toString(command.command());

        AddedEvent event = new AddedEvent();
        event.setCommand(string);
        event.setMessage("added: " + string);
        EventBus.post(event);
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
