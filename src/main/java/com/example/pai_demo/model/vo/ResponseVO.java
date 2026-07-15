package com.example.pai_demo.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author GilbertYoung
 * @date 2026/07/07 16:10
 */
@Data
@AllArgsConstructor
public class ResponseVO<T> {
    /**
     * 请求码
     */
    @ApiModelProperty(value = "请求码")
    private Integer code;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String msg;
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T data;

    public static <T> ResponseVO<T> createSuc(T o) {
        return new ResponseVO<T>(200, null, o);
    }

    public static <T> ResponseVO<T> createErr(String msg) {
        return new ResponseVO<T>(-1, msg, null);
    }

    public static <T> ResponseVO<T> createErr(int code, String msg) {
        return new ResponseVO<T>(code, msg, null);
    }

    public static <T> ResponseVO<T> createTokenAuthorizedErr() {
        return new ResponseVO<>(5, "Token失效或不存在!", null);
    }
}
