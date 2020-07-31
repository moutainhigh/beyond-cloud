package com.beyond.cloud.exception;

/**
 * @author lucifer
 * @date 2020/7/31 15:53
 */
public class NotFoundException {

    private int code;

    public static final NotFoundException instance = new NotFoundException(404);

    public NotFoundException(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }
}
