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

import ibgroup.web.core.clientportal.gw.Config;
import ibgroup.web.core.clientportal.gw.GatewayStart;
import ibgroup.web.core.clientportal.gw.GatewayVerticle;
import ibgroup.web.core.clientportal.gw.utils.OptionUtils;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import ua.com.gfalcon.invest.ibkr.client.config.GateWayConfig;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class GateWay extends Thread {

    private static Config config = new Config();
    private static final int EXIT_FAILED = 1;
    private static final Logger logger = LoggerFactory.getLogger(GatewayStart.class);

    @Override
    public void run() {
        System.setProperty("vertx.disableDnsResolver","true");
        System.setProperty("java.net.preferIPv4Stack","true");
        System.setProperty("vertx.logger-delegate-factory-class-name","io.vertx.core.logging.SLF4JLogDelegateFactory");
        System.setProperty("nologback.statusListenerClass","ch.qos.logback.core.status.OnConsoleStatusListener");
        System.setProperty("nolog4j.debug","true");
        System.setProperty("nolog4j2.debug","true");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("gw/root/conf.yaml");
        startGateway(url);
    }

    private void startGateway(URL url) {
        String file = url.getPath().substring(1);
        try {
            InputStream in = GateWay.class.getClassLoader().getResourceAsStream(GateWayConfig.CLIENTPORTAL_CONF_YAML_PATH);

            Throwable var5 = null;

            try {
                Yaml yaml = new Yaml();
                config = (Config)yaml.loadAs(in, Config.class);
                String sslCert = config.getSslCert();
                System.out.println(config);
            } catch (Throwable var19) {
                var5 = var19;
                throw var19;
            } finally {
                if (in != null) {
                    if (var5 != null) {
                        try {
                            in.close();
                        } catch (Throwable var18) {
                            var5.addSuppressed(var18);
                        }
                    } else {
                        in.close();
                    }
                }

            }
        } catch (Exception var23) {
            System.err.println("Failed to load configuration file: " + file + "\n" + var23.getMessage());
//            failed("loading configuration file error:" + var23.getMessage());
        }


//        String host = cmd.getOptionValue("host", "");
//        if (!host.isEmpty()) {
//            config.setProxyRemoteHost(host);
//        }

//        if (cmd.hasOption("port")) {
//            try {
//                int port = ((Number)cmd.getParsedOptionValue("port")).intValue();
//                config.setListenPort(port);
//            } catch (ParseException var17) {
//                System.err.println("failed to parse port value");
//            }
//        }
//
//        if (cmd.hasOption("nossl")) {
//            config.setListenSsl(false);
//        }

        config.setSslCert(url.getPath().substring(1).replace("/", File.separator).replace("conf.yaml", config.getSslCert()));

        Vertx vertx = Vertx.vertx(config.getVertxOptions());
        OptionUtils.setSharedGlobalConfig(vertx, config);
//        logger.info("config {} {}", new Object[]{file, (new Yaml()).dump(config)});
        vertx.deployVerticle(new GatewayVerticle());
    }
}
