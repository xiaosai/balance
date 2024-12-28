package com.example.balance.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.balance.common.Response;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BalanceException.class)
    public Response<Object> onIncidentException(BalanceException e) {
        return Response.fail(e.getCode(), e.getMessage());
    }
}
