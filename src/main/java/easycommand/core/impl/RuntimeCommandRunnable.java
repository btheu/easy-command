package easycommand.core.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuntimeCommandRunnable extends SimpleCommandRunnable {

    protected Process process;

    @Override
    protected void start() throws Exception {

        process = Runtime.getRuntime().exec(command.command());

        log.debug("the output stream is {}", process.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            log.debug("The inout stream is {}", s);
            this.onProgress(s);
        }

        log.debug("Exit status: {}", process.exitValue());
    }

    @Override
    public void kill() {
        this.process.destroyForcibly();
    }

}
