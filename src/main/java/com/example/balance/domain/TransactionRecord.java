package com.example.balance.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
@Data
public class TransactionRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 来源账户
     */
    private String sourceAccount;

    /**
     * 目标账户
     */
    private String destinationAccount;

    /**
     * 转账金额，单位分
     */
    private Long amount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date modifiedTime;
}
