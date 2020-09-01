package com.beyond.cloud.svc.business.advice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.exception.*;
import com.google.common.base.Throwables;
import com.netflix.client.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * flash correct
 *
 * @author Gent Liu
 * @date 2019/6/4 18:34
 */
@ControllerAdvice
public class HandlerExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerExceptionAdvice.class);
    private static final String GENERIC_ERROR_MESSAGE = "服务端发生未知错误";

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Object handleException(final Exception e) {
        LOGGER.error(e.getMessage(), e);
        return this.formatException(e);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleException(final MethodArgumentNotValidException result) {
        List<ObjectError> errors = result.getBindingResult().getAllErrors();
        ObjectError error = errors.get(0);
        return ApiResult.error(error.getDefaultMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(RuntimeException.class)
    public Object handleException(final RuntimeException e) {
        Throwable cause = e.getCause();
        if (cause instanceof ClientException) {
            return ApiResult.error(HttpStatus.SERVICE_UNAVAILABLE.value(), e.getMessage());
        }
        return ApiResult.error(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public Object handleException(final BusinessException e) {
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ApiException.class)
    public Object handleException(final ApiException e) {
        LOGGER.error(e.getMessage(), e);
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ForbiddenException.class)
    public Object handleException(final ForbiddenException e) {
        LOGGER.error(e.getMessage(), e);
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotFoundException.class)
    public Object handleException(final NotFoundException e) {
        LOGGER.error(e.getMessage(), e);
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UnauthorizedException.class)
    public Object handleException(final UnauthorizedException e) {
        LOGGER.error(e.getMessage(), e);
        return ApiResult.error(e.getCode(), e.getMessage());
    }


    private Map formatException(final Exception e) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", -1);
        data.put("message", LOGGER.isDebugEnabled() ? e.getMessage() : GENERIC_ERROR_MESSAGE);
        if (LOGGER.isDebugEnabled()) {
            this.appendException(data, e);
        }
        return data;
    }

    private void appendException(final Map<String, Object> node, final Throwable e) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("message", e.getMessage());
        data.put("class", e.getClass().getName());
        data.put("stackTrace", Throwables.getStackTraceAsString(e)
            .replace("\r\n", "\n")
            .replace("\r", "\n")
            .replace("\t", "    ")
            .split("\n"));
        if (e.getCause() != null) {
            this.appendException(data, e.getCause());
        }
        node.put("exception", data);
    }

}
