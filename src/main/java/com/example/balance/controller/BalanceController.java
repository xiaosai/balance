package com.example.balance.controller;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.balance.common.Response;
import com.example.balance.domain.AccountBalance;
import com.example.balance.service.BalanceService;
import com.example.balance.vo.TransferVO;


/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
@RestController
@RequestMapping("/api/balance")
@Validated
public class BalanceController {

    @Resource
    private BalanceService balanceService;

    @PostMapping("/transfer")
    public Response<Void> transfer(@Validated @RequestBody TransferVO transferVO) {
        balanceService.transfer(transferVO);
        return Response.success(null);
    }

    @GetMapping("/{account}")
    public Response<AccountBalance> get(@PathVariable String account) {
        return Response.success(balanceService.get(account));
    }
}
