package btheu.easy.command.core.impl;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import btheu.easy.command.bean.Command;
import btheu.easy.command.core.CommandExecutor;
import btheu.easy.command.core.CommandRunnable;
import btheu.easy.command.event.AddedEvent;
import btheu.easy.command.event.EventBus;
import btheu.easy.command.event.KillEvent;

public class DefaultCommandExecutor implements CommandExecutor {

    protected static ExecutorService executor = Executors.newFixedThreadPool(2);

    @Override
    public void kill(String commandId) {
        EventBus.post(new KillEvent(commandId));
    }

    @Override
    public void submit(Command command) {

        this.onSubmit(command);

        CommandRunnable runnable;
        if ("yes".equalsIgnoreCase(System.getenv("use.ssh"))) {
            runnable = new SshCommandRunnable();
        } else {
            runnable = new RuntimeCommandRunnable();
        }

        EventBus.subscribe(runnable);

        runnable.prepare(command);

        executor.execute(runnable);
    }

    protected void onSubmit(Command command) {
        String string = Arrays.toString(command.commands);

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
