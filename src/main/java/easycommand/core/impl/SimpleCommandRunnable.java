package easycommand.core.impl;

import java.util.Arrays;

import easycommand.core.Command;
import easycommand.core.CommandRunnable;
import easycommand.event.EventBus;
import easycommand.event.FinishedEvent;
import easycommand.event.ProgressEvent;
import easycommand.event.StartedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCommandRunnable implements CommandRunnable {

    protected Command command;

    @Override
    public void prepare(Command command) {
        this.command = command;
    }

    @Override
    public void run() {

        this.onStart();

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

            String string = Arrays.toString(command.command());

            this.onProgress(string + " " + i);

            long waitSec = (long) (2 + Math.random() * 1.0);

            try {
                Thread.sleep(waitSec * 1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onFinish() {
        String string = Arrays.toString(command.command());

        FinishedEvent event = new FinishedEvent();
        event.setCommand(string);
        event.setMessage("finished: " + string);

        EventBus.post(event);

    }

    protected void onError() {
        this.onFinish();
    }

    protected void onProgress(String message) {
        String string = Arrays.toString(command.command());

        ProgressEvent event = new ProgressEvent();
        event.setCommand(string);
        event.setMessage("in progress: " + message);

        EventBus.post(event);

    }

    protected void onStart() {
        String string = Arrays.toString(command.command());

        StartedEvent event = new StartedEvent();
        event.setCommand(string);
        event.setMessage("starting: " + string);

        EventBus.post(event);

    }

}
