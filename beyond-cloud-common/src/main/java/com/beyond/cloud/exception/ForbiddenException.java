package com.beyond.cloud.exception;

/**
 * @author lucifer
 * @date 2020/7/31 15:53
 */
public class ForbiddenException extends RuntimeException {

    private int code;

    public static final ForbiddenException instance = new ForbiddenException(403);

    public ForbiddenException(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }
}
