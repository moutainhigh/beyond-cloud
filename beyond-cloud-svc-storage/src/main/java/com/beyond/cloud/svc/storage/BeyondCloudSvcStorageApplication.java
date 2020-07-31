package com.beyond.cloud.svc.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@ComponentScan(value = "com.beyond.cloud")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BeyondCloudSvcStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeyondCloudSvcStorageApplication.class, args);
    }

}
