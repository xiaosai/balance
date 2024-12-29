package com.example.balance.service.impl;

import static com.example.balance.common.Constants.RESPONSE_CODE_ACCOUNT_NOT_EXIST;
import static com.example.balance.common.Constants.RESPONSE_CODE_BALANCE_INSUFFICIENT;
import static com.example.balance.common.Constants.RESPONSE_CODE_DB_FAIL;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.balance.domain.AccountBalance;
import com.example.balance.domain.TransactionRecord;
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

    /**
     * 转账操作
     * 根据 account 排序，然后加锁，避免并发时候死锁.
     * 增加事务，保证一致性
     *
     * @param transferVO 转账 vo 对象
     */
    @Override
    @Transactional
    public void transfer(TransferVO transferVO) {
        int cmp = transferVO.getSourceAccount().compareTo(transferVO.getDestinationAccount());
        if (0 == cmp) {
            // source account and target account is the same
            return;
        }

        // 1. get balance by account
        AccountBalance source;
        AccountBalance target;
        if (cmp < 0) {
            source = accountBalanceMapper.selectByAccountForUpdate(transferVO.getSourceAccount());
            target = accountBalanceMapper.selectByAccountForUpdate(transferVO.getDestinationAccount());
        } else {
            target = accountBalanceMapper.selectByAccountForUpdate(transferVO.getDestinationAccount());
            source = accountBalanceMapper.selectByAccountForUpdate(transferVO.getSourceAccount());
        }
        if (source == null) {
            throw new BalanceException(RESPONSE_CODE_ACCOUNT_NOT_EXIST,
                    String.format("account %s not exist", transferVO.getSourceAccount()));
        }
        if (target == null) {
            throw new BalanceException(RESPONSE_CODE_ACCOUNT_NOT_EXIST,
                    String.format("account %s not exist", transferVO.getDestinationAccount()));
        }

        // 2. validate source balance is enough
        if (source.getBalance() < transferVO.getAmount()) {
            throw new BalanceException(RESPONSE_CODE_BALANCE_INSUFFICIENT, "balance is insufficient");
        }

        // 3. insert transaction record, for check
        Date now = new Date();
        TransactionRecord record = new TransactionRecord();
        record.setTransactionId(transferVO.getTransactionId());
        record.setSourceAccount(transferVO.getSourceAccount());
        record.setDestinationAccount(transferVO.getDestinationAccount());
        record.setAmount(transferVO.getAmount());
        record.setCreatedTime(now);
        record.setModifiedTime(now);
        int insert = transactionRecordMapper.insert(record);
        if (insert != 1) {
            throw new BalanceException(RESPONSE_CODE_DB_FAIL, "insert transaction record failed");
        }

        // 4. transfer
        source.setBalance(source.getBalance() - transferVO.getAmount());
        source.setModifiedTime(now);

        int update = accountBalanceMapper.updateById(source);
        if (update != 1) {
            throw new BalanceException(RESPONSE_CODE_DB_FAIL, "update source balance failed");
        }

        target.setBalance(target.getBalance() + transferVO.getAmount());
        target.setModifiedTime(now);
        update = accountBalanceMapper.updateById(target);
        if (update != 1) {
            throw new BalanceException(RESPONSE_CODE_DB_FAIL, "update destination balance failed");
        }
    }

    @Override
    public AccountBalance get(String account) {
        return getByAccount(account);
    }

    private AccountBalance getByAccount(String account) {
        QueryWrapper<AccountBalance> query = new QueryWrapper<>();
        query.eq("account", account);
        AccountBalance accountBalance = accountBalanceMapper.selectOne(query);
        if (accountBalance == null) {
            throw new BalanceException(RESPONSE_CODE_ACCOUNT_NOT_EXIST, "account not exist");
        }
        return accountBalance;
    }
}
