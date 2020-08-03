package com.beyond.cloud.svc.business.exception;

/**
 * flash-correct
 *
 * @author Gent Liu
 * @date 2019/6/4 18:39
 */

public class ApiException extends RuntimeException {

    private static final int CODE_UNKNOWN_ERROR = -1;

    private int code;

    public ApiException(final int code) {
        super();
        this.code = code;
    }

    public ApiException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    public ApiException(final String message) {
        this(CODE_UNKNOWN_ERROR, message);
    }

    public ApiException(final int code, final Throwable cause) {
        this(code, cause.getMessage(), cause);
    }

    public ApiException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
