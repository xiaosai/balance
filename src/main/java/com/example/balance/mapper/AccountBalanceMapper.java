package com.example.balance.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.balance.domain.AccountBalance;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
public interface AccountBalanceMapper extends BaseMapper<AccountBalance> {

    @Select("SELECT * FROM account_balance WHERE account = #{account} FOR UPDATE")
    AccountBalance selectByAccountForUpdate(@Param("account") String account);
}
