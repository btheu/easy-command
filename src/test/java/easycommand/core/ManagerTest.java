package easycommand.core;

import org.junit.Test;

import easycommand.core.impl.BasicCommand;
import easycommand.core.impl.DefaultCommandExecutor;
import easycommand.event.EventBus;
import easycommand.event.ProgressEvent;
import easycommand.event.StartEvent;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

@Slf4j
@Listener(references = References.Strong)
public class ManagerTest {

    @Test
    public void test() {

        EventBus.subscribe(this);

        Command cmd1 = new BasicCommand("Ma commande", "1");

        CommandExecutor manager = new DefaultCommandExecutor();

        manager.submit(cmd1);

        Command cmd2 = new BasicCommand("Ma commande", "2");

        manager.submit(cmd2);

        manager.submit(cmd1);

        manager.waitForEnd();

    }

    @Handler
    public void handle(StartEvent event) {
        log.info("{} ", event.getMessage());
    }

    @Handler
    public void handle(ProgressEvent event) {
        log.info("{} ", event.getMessage());
    }

}
