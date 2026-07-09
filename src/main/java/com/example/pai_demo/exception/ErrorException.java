package com.example.pai_demo.exception;

import lombok.Getter;


/**
 * @author DrGilbert
 */
@Getter
public class ErrorException extends RuntimeException {
    private final ErrorCode code;

    public ErrorException(ErrorCode code) {
        super(code.getMsg());
        this.code = code;
    }

    public ErrorException(ErrorCode code, Throwable e) {
        super(code.getMsg(), e);
        this.code = code;
    }

    public ErrorException(ErrorCode code, String msg) {
        super(msg);
        this.code = code;
    }

    public ErrorException(ErrorCode code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }
}
