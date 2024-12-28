package com.example.balance.exception;

import lombok.Data;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
@Data
public class BalanceException extends RuntimeException {

    private final int code;

    public BalanceException(int code, String message) {
        super(message);
        this.code = code;
    }
}
