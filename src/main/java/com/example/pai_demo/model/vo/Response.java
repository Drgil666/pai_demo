package com.example.pai_demo.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author GilbertYoung
 * @date 2026/07/07 16:10
 */
@Data
@AllArgsConstructor
public class Response<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Response<T> createSuc(T o) {
        return new Response<T>(200, null, o);
    }

    public static <T> Response<T> createErr(String msg) {
        return new Response<T>(-1, msg, null);
    }

    public static <T> Response<T> createErr(int code, String msg) {
        return new Response<T>(code, msg, null);
    }

    public static <T> Response<T> createTokenAuthorizedErr() {
        return new Response<>(5, "Token失效或不存在!", null);
    }
}
