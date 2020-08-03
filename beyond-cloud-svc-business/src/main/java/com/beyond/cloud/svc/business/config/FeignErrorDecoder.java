package com.beyond.cloud.svc.business.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.svc.business.exception.ApiException;
import com.beyond.cloud.utils.JsonUtils;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignErrorDecoder.class);

    @Override
    public Exception decode(final String methodKey,final Response response) {
        LOGGER.debug("MethodKey: {}", methodKey);
        LOGGER.debug("Status: {}", response.status());
        LOGGER.debug("Reason: {}", response.reason());
        LOGGER.debug("Headers: {}", response.headers());
        LOGGER.debug("Body: {}", response.body());

        if (response.body() == null) {
            return new ApiException("服务端响应：" + response.status());
        }

        final String contentType = this.getHeader(response, HttpHeaders.CONTENT_TYPE);
        if (contentType == null) {
            return new ApiException("服务端响应：" + response.status());
        } else if (!contentType.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            return new ApiException(response.body().toString());
        }

        ApiResult ApiResult;
        try {
            Reader reader = response.body().asReader(StandardCharsets.UTF_8);
            ApiResult = JsonUtils.deserialize(reader, ApiResult.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ApiException(ApiResult.getCode(), ApiResult.getMessage());
    }

    private String getHeader(final Response response, final String name) {
        final Collection<String> values = response.headers().get(name);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.stream().findFirst().orElse(null);
    }

}
