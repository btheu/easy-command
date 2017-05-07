package easycommand.core.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuntimeCommandRunnable extends SimpleCommandRunnable {

    @Override
    protected void start() throws Exception {

        log.debug("Executing {}", command.command()[0]);

        Process process = Runtime.getRuntime().exec(command.command());

        log.debug("the output stream is {}", process.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s;
        while ((s = reader.readLine()) != null) {
            if (haveToStop) {
                log.info("Command had to stop", s);
                break;
            }
            log.debug("The inout stream is {}", s);
            this.onProgress(s);
        }

    }

}
