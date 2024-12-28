package com.example.balance.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
@Data
public class Response<T> {

    private Integer code;
    private String message;
    private T data;

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response<Object> fail(Integer code, String message) {
        return new Response(code, message, null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(Constants.RESPONSE_CODE_SUC, Constants.RESPONSE_MSG_SUC, data);
    }
}
