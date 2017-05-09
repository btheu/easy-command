package btheu.easy.command.core.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

@Slf4j
public class SshCommandRunnable extends SimpleCommandRunnable {

    Command process;

    @Override
    protected void start() throws Exception {

        try (final SSHClient ssh = new SSHClient()) {

            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(System.getenv("host.name"), Integer.parseInt(System.getenv("host.port")));
            ssh.authPassword(System.getenv("user.name"), System.getenv("user.password"));

            try (final Session session = ssh.startSession()) {

                process = session.exec(String.join(" ", command.getCommands()));

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String s;
                while ((s = reader.readLine()) != null) {
                    log.debug("The inout stream is {}", s);
                    this.onProgress(s);
                }

                process.join(5, TimeUnit.SECONDS);

                log.debug("Exit status: {}", process.getExitStatus());
            }
        }
    }

    @Override
    public void kill() {
        try {
            this.process.close();
        } catch (TransportException | ConnectionException e) {
            e.printStackTrace();
        }
    }

}
