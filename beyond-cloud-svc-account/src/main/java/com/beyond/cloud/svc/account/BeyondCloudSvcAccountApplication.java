package com.beyond.cloud.svc.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BeyondCloudSvcAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeyondCloudSvcAccountApplication.class, args);
    }

}
