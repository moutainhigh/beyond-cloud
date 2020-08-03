package com.beyond.cloud.exception;

/**
 * @author lucifer
 * @date 2020/7/31 15:53
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(final String message) {
        super(message);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }
}
