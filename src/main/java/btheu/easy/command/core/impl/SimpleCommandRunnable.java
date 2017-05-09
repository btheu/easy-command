package btheu.easy.command.core.impl;

import java.util.Arrays;

import btheu.easy.command.bean.Command;
import btheu.easy.command.core.CommandRunnable;
import btheu.easy.command.event.EventBus;
import btheu.easy.command.event.FinishedEvent;
import btheu.easy.command.event.KillEvent;
import btheu.easy.command.event.ProgressEvent;
import btheu.easy.command.event.StartedEvent;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

@Slf4j
@Listener
public class SimpleCommandRunnable implements CommandRunnable {

    protected Command command;

    @Override
    public void prepare(Command command) {
        this.command = command;
    }

    public void kill() {
    }

    @Override
    public void run() {

        this.onStart();

        log.debug("Executing {}", String.join(" ", command.getCommands()));

        try {
            this.start();
            this.onFinish();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            this.onError();
        }

    }

    protected void start() throws Exception {
        int i = 0;
        while ((i++) < 20) {

            String string = Arrays.toString(command.getCommands());

            this.onProgress(string + " " + i);

            long waitSec = (long) (2 + Math.random() * 1.0);

            try {
                Thread.sleep(waitSec * 1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Handler
    protected void handle(KillEvent event) {
        String string = Arrays.toString(command.getCommands());

        if (string.equals(event.getCommandId())) {
            this.kill();
        }
    }

    protected void onFinish() {
        String string = Arrays.toString(command.getCommands());

        FinishedEvent event = new FinishedEvent();
        event.setCommand(string);
        event.setMessage("finished: " + string);

        EventBus.post(event);

    }

    protected void onError() {
        this.onFinish();
    }

    protected void onProgress(String message) {
        String string = Arrays.toString(command.getCommands());

        ProgressEvent event = new ProgressEvent();
        event.setCommand(string);
        event.setMessage("in progress: " + message);

        EventBus.post(event);

    }

    protected void onStart() {
        String string = Arrays.toString(command.getCommands());

        StartedEvent event = new StartedEvent();
        event.setCommand(string);
        event.setMessage("starting: " + string);

        EventBus.post(event);

    }

}
