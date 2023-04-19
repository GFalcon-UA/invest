package ua.com.gfalcon.invest.ibkr.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.com.gfalcon.invest.ibkr.client.service.GateWay;
import ua.com.gfalcon.invest.ibkr.client.service.GateWayRunner;

@SpringBootApplication
public class IbkrClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(IbkrClientApplication.class, args);

        GateWay gateWay = new GateWay();
        gateWay.start();
    }

}
