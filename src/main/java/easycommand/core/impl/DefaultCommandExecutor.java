package easycommand.core.impl;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import easycommand.bean.Command;
import easycommand.core.CommandExecutor;
import easycommand.core.CommandRunnable;
import easycommand.event.AddedEvent;
import easycommand.event.EventBus;
import easycommand.event.KillEvent;

public class DefaultCommandExecutor implements CommandExecutor {

    protected static ExecutorService executor = Executors.newFixedThreadPool(2);

    @Override
    public void kill(String commandId) {
        EventBus.post(new KillEvent(commandId));
    }

    @Override
    public void submit(Command command) {

        this.onSubmit(command);

        // CommandRunnable runnable = new SimpleCommandRunnable();
        CommandRunnable runnable = new RuntimeCommandRunnable();

        EventBus.subscribe(runnable);

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
