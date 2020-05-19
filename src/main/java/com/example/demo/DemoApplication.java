package com.example.demo;

import com.example.demo.bridge.BridgeBasicManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 李康龙
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        BridgeBasicManager.init();
        SpringApplication.run(DemoApplication.class, args);
    }

}
