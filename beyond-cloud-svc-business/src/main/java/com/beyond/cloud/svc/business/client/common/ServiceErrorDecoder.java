package com.beyond.cloud.svc.business.client.common;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author lucifer
 */
public class ServiceErrorDecoder implements ErrorDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceErrorDecoder.class);
    private static final String X_RESULT_CODE = "X-Result-Code";
    private static final String X_RESULT_MESSAGE = "X-Result-Message";

//    @SuppressWarnings("rawtypes")
    @Override
    public Exception decode(final String methodKey, final Response response) {
        return null;
//        LOGGER.debug("MethodKey: {}", methodKey);
//        LOGGER.debug("Status: {}", response.status());
//        LOGGER.debug("Reason: {}", response.reason());
//        LOGGER.debug("Headers: {}", response.headers());
//        LOGGER.debug("Body: {}", response.body());
//
//        final Map<String, Collection<String>> headers = response.headers();
//
//        if (response.body() == null) {
//            if (headers.containsKey(X_RESULT_CODE) && headers.containsKey(X_RESULT_MESSAGE)) {
//                int code = Integer.parseInt(headers.get(X_RESULT_CODE).stream().findFirst().orElse("-1"));
//                String message = headers.get(X_RESULT_MESSAGE).stream().findFirst().orElse("");
//                return new ApiException(code, message);
//            }
//            return new ApiException("服务端响应：" + response.status());
//        }
//
//        final String contentType = this.getHeader(response, HttpHeaders.CONTENT_TYPE);
//        if (contentType == null) {
//            return new ApiException("服务端响应：" + response.status());
//        } else if (!contentType.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
//            return new ApiException(response.body().toString());
//        }
//
//        Result result;
//        try {
//            Reader reader = response.body().asReader(StandardCharsets.UTF_8);
//            result = JsonUtils.deserialize(reader, Result.class);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return new ApiException(result.getCode(), result.getMessage());
    }
//
//    private String getHeader(final Response response, final String name) {
//        final Collection<String> values = response.headers().get(name);
//        if (values == null || values.isEmpty()) {
//            return null;
//        }
//        return values.stream().findFirst().orElse(null);
//    }

}
