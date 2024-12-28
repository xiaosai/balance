package com.example.balance.service.impl;

import static com.example.balance.common.Constants.RESPONSE_CODE_ACCOUNT_NOT_EXIST;
import static com.example.balance.common.Constants.RESPONSE_CODE_BALANCE_NOT_ENOUGH;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.balance.domain.AccountBalance;
import com.example.balance.exception.BalanceException;
import com.example.balance.mapper.AccountBalanceMapper;
import com.example.balance.mapper.TransactionRecordMapper;
import com.example.balance.service.BalanceService;
import com.example.balance.vo.TransferVO;


/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
@Service
public class BalanceServiceImpl implements BalanceService {

    @Resource
    private AccountBalanceMapper accountBalanceMapper;

    @Resource
    private TransactionRecordMapper transactionRecordMapper;

    @Override
    public void transfer(TransferVO transferVO) {
        AccountBalance source = getByAccount(transferVO.getSourceAccount());
        if (source == null) {
            throw new BalanceException(RESPONSE_CODE_ACCOUNT_NOT_EXIST,
                    String.format("account %s not exist", transferVO.getSourceAccount()));
        }

        AccountBalance target = getByAccount(transferVO.getDestinationAccount());
        if (target == null) {
            throw new BalanceException(RESPONSE_CODE_ACCOUNT_NOT_EXIST,
                    String.format("account %s not exist", transferVO.getDestinationAccount()));
        }

        if (source.getBalance() < transferVO.getAmount()) {
            throw new BalanceException(RESPONSE_CODE_BALANCE_NOT_ENOUGH, "balance is insufficient");
        }

        source.setBalance(source.getBalance() - transferVO.getAmount());
        accountBalanceMapper.updateById(source);

        target.setBalance(target.getBalance() + transferVO.getAmount());
        accountBalanceMapper.updateById(target);
    }

    @Override
    public AccountBalance get(String account) {
        return getByAccount(account);
    }


    private AccountBalance getByAccount(String account) {
        QueryWrapper<AccountBalance> query = new QueryWrapper<>();
        query.eq("account", account);
        return accountBalanceMapper.selectOne(query);
    }
}
