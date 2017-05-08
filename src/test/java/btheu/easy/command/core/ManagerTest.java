package btheu.easy.command.core;

import org.junit.Test;

import btheu.easy.command.bean.Command;
import btheu.easy.command.core.CommandExecutor;
import btheu.easy.command.core.impl.BasicCommand;
import btheu.easy.command.core.impl.DefaultCommandExecutor;
import btheu.easy.command.event.EventBus;
import btheu.easy.command.event.ProgressEvent;
import btheu.easy.command.event.StartedEvent;
import lombok.extern.slf4j.Slf4j;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

@Slf4j
@Listener
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
    public void handle(StartedEvent event) {
        log.info("{} ", event.getMessage());
    }

    @Handler
    public void handle(ProgressEvent event) {
        log.info("{} ", event.getMessage());
    }

}
