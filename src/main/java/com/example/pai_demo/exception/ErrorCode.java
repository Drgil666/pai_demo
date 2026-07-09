package com.example.pai_demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author DrGilbert
 */

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 参数非法
     */
    UNKNOWN_ERROR(1, "unknown error", LogLevel.ERROR),
    SYSTEM_ERROR(2, "system error", LogLevel.ERROR),
    BIZ_PARAM_ILLEGAL(3, "biz param illegal", LogLevel.INFO),
    INNER_PARAM_ILLEGAL(4, "inner param illegal", LogLevel.ERROR),
    TOKEN_AUTHORIZE_ILLEGAL(5, "token authorize illegal", LogLevel.ERROR);
    private final Integer code;
    private final String msg;
    private final LogLevel logLevel;
}
