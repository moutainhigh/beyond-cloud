package com.beyond.cloud.svc.business.client.common;

import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author lucifer
 */
public class FeignConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignConfig.class);

    @Value("${app.service-security.internal-secret}")
    private String serviceSecret;

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ServiceErrorDecoder();
    }

}
