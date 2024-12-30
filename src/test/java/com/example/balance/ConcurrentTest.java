package com.example.balance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.balance.common.Constants;
import com.example.balance.common.JsonUtil;
import com.example.balance.domain.AccountBalance;
import com.example.balance.mapper.AccountBalanceMapper;
import com.example.balance.mapper.TransactionRecordMapper;
import com.example.balance.vo.TransferVO;

import lombok.SneakyThrows;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-30
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ConcurrentTest {

    @Autowired
    private MockMvc mockMvc;

    @Resource
    private TransactionRecordMapper transactionRecordMapper;

    @Resource
    private AccountBalanceMapper accountBalanceMapper;

    private AtomicInteger counter = new AtomicInteger(0);
    private AtomicInteger finCounter = new AtomicInteger(0);


    @AfterEach
    public void clearRecord() {
        transactionRecordMapper.delete(null);
    }

    @Test
    void whenConcurrentTest_shouldSuccess() throws Exception {
        // 1. validate sum
        testSumBalance();

        // 2. concurrent test
        int total = 1000;
        int concurrency = 10;
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        for (int i = 0; i < total; i++) {
            executor.submit(this::testRandomTransfer);
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // 3. validate sum
        testSumBalance();
    }

    @SneakyThrows
    private void testRandomTransfer() {
        String sourceAccount = randomAccount();
        String targetAccount = randomAccount();
        String transactionId = "T_" + counter.incrementAndGet();
        long amount = randomAmount();

        TransferVO transferVO = new TransferVO();
        transferVO.setAmount(amount);
        transferVO.setSourceAccount(sourceAccount);
        transferVO.setDestinationAccount(targetAccount);
        transferVO.setTransactionId(transactionId);

        mockMvc.perform(post("/api/balance/transfer")
                .content(JsonUtil.toJsonString(transferVO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC));

        // System.out.println("finished transfer:" + JsonUtil.toJsonString(transferVO));
        finCounter.incrementAndGet();
    }

    private long randomAmount() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }

    private String randomAccount() {
        Random random = new Random();
        int accountNum = random.nextInt(5) + 101;
        return "ACCOUNT_" + accountNum;
    }


    private void testSumBalance() {
        List<AccountBalance> balanceList = accountBalanceMapper.selectList(null);
        long sum = 0;
        for (AccountBalance ab : balanceList) {
            Assertions.assertTrue(ab.getBalance() >= 0, "balance can not be negative");
            sum += ab.getBalance();
        }
        Assertions.assertEquals(5000, sum, "balance sum should be 5000");
    }
}
