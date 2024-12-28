package com.example.balance.service;

import com.example.balance.domain.AccountBalance;
import com.example.balance.vo.TransferVO;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
public interface BalanceService {

    void transfer(TransferVO transferVO);

    AccountBalance get(String account);
}
