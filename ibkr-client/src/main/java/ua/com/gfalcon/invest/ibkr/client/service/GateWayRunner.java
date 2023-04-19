/*
 * Copyright (c) 2016-2023 Oleksii Khalikov @GFalcon-UA (http://gfalcon.com.ua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.gfalcon.invest.ibkr.client.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.gfalcon.invest.ibkr.client.exception.IbkrGatewayProcessException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystems;

public class GateWayRunner {

    private static final Logger log = LoggerFactory.getLogger(GateWayRunner.class);

    private final boolean isWindows;

    private final ProcessBuilder processBuilder;

    public GateWayRunner() {
        isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        processBuilder = new ProcessBuilder();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("gw");
        if(url == null) {
            log.error("no found");
            return;
        }
        String path = url.getPath();

        String separator = FileSystems.getDefault().getSeparator();
        StringBuilder cmd = new StringBuilder()
                .append(isWindows ? "cmd /c start " : "")
                .append(path)
                .append(separator)
                .append("bin")
                .append(separator)
                .append("run.")
                .append(isWindows ? "bat" : "sh")
                .append(" ")
                .append(path)
                .append(separator)
                .append("root")
                .append(separator)
                .append("conf.yaml");
//        try {
//            Runtime.getRuntime().exec(cmd.toString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        String runtime = "";
        String configFile = "";
        if(isWindows) {
            runtime = "root;dist\\ibgroup.web.core.iblink.router.clientportal.gw.jar;build\\lib\\runtime\\*";
            configFile = "\\root\\conf.yaml";

//            processBuilder.command("cmd", "/c", "start", "bin\\run.bat root\\conf.yaml");
        } else {
            runtime = "$config_path:dist/ibgroup.web.core.iblink.router.clientportal.gw.jar:build/lib/runtime/*";
            configFile = "/root/conf.yaml";
//            processBuilder.command("bin/run.sh root/conf.yaml");
        }
        String cmd2 = "-jar" +
                " -Dvertx.disableDnsResolver=true" +
                " -Djava.net.preferIPv4Stack=true" +
                " -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory" +
                " -Dnologback.statusListenerClass=ch.qos.logback.core.status.OnConsoleStatusListener" +
                " -Dnolog4j.debug=true" +
                " -Dnolog4j2.debug=true" +
                " -classpath " + runtime +
                " ibgroup.web.core.clientportal.gw.GatewayStart --conf .." + configFile;
        processBuilder.command("java", cmd2);
        processBuilder.directory(new File(path));
    }

    private Process process;

    public void start() {
        try {
            process = processBuilder.start();
            log.info("IBKR GW Process starts. Process PID {}", process.pid());
            log.info("IBKR GW Process info : {}", process.info());
            StringBuilder output = new StringBuilder();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                log.info(line);
//            }
//            int exitval = process.waitFor();
//            if(exitval == 0) {
//                log.info("IBKR GW Process is closed Successfully");
//            } else {
//                log.warn("IBKR GW Process is closed with code {}", exitval);
//            }
        } catch (IOException /*| InterruptedException*/ e) {
            log.error("IBKR GateWay Process exception", e);
            throw new IbkrGatewayProcessException(e);
        }
    }

    public void run() {
        System.setProperty("vertx.disableDnsResolver","true");
        System.setProperty("java.net.preferIPv4Stack","true");
        System.setProperty("vertx.logger-delegate-factory-class-name","io.vertx.core.logging.SLF4JLogDelegateFactory");
        System.setProperty("nologback.statusListenerClass","ch.qos.logback.core.status.OnConsoleStatusListener");
        System.setProperty("nolog4j.debug","true");
        System.setProperty("nolog4j2.debug","true");
    }
}
