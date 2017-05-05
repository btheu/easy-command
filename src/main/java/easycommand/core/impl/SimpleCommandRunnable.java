package easycommand.core.impl;

import java.util.Arrays;

import easycommand.core.Command;
import easycommand.core.CommandRunnable;
import easycommand.event.EventBus;
import easycommand.event.FinishEvent;
import easycommand.event.ProgressEvent;
import easycommand.event.StartEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCommandRunnable implements CommandRunnable {

    private Command command;

    @Override
    public void prepare(Command command) {
        this.command = command;
    }

    @Override
    public void run() {

        this.onStart();

        int i = 0;
        while ((i++) < 20) {

            String string = Arrays.toString(command.command());

            this.onProgress(string + " " + i);

            // log.info("running {}", string);

            long waitSec = (long) (2 + Math.random() * 1.0);

            try {
                Thread.sleep(waitSec * 1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.onFinish();
    }

    protected void onFinish() {
        String string = Arrays.toString(command.command());

        FinishEvent event = new FinishEvent();

        event.setMessage("finished : " + string);

        EventBus.post(event);

    }

    protected void onProgress(String string) {
        ProgressEvent event = new ProgressEvent();

        event.setMessage("progress : " + string);

        EventBus.post(event);

    }

    protected void onStart() {
        String string = Arrays.toString(command.command());

        StartEvent event = new StartEvent();

        event.setMessage("starting command: " + string);

        EventBus.post(event);

    }

}
