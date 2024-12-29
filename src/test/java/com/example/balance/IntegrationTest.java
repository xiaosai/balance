package com.example.balance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import javax.annotation.Resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.balance.common.Constants;
import com.example.balance.common.JsonUtil;
import com.example.balance.mapper.TransactionRecordMapper;
import com.example.balance.vo.TransferVO;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-28
 */
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Resource
    private TransactionRecordMapper transactionRecordMapper;

    @AfterEach
    public void clearRecord() {
        transactionRecordMapper.delete(null);
    }

    @Test
    void whenPerformIntegrationTest_shouldSuccess() throws Exception {

        String sourceAccount = "ACCOUNT_101";
        String targetAccount = "ACCOUNT_102";

        // 1. init balance
        mockMvc.perform(get("/api/balance/{account}", sourceAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC))
                .andExpect(jsonPath("$.data.balance").value(1000));

        mockMvc.perform(get("/api/balance/{account}", targetAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC))
                .andExpect(jsonPath("$.data.balance").value(1000));

        // 2. transfer 500
        TransferVO transferVO = new TransferVO();
        transferVO.setAmount(500L);
        transferVO.setSourceAccount(sourceAccount);
        transferVO.setDestinationAccount(targetAccount);
        transferVO.setTransactionId("T001");
        mockMvc.perform(post("/api/balance/transfer")
                        .content(JsonUtil.toJsonString(transferVO))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC));

        // 3. new balance
        mockMvc.perform(get("/api/balance/{account}", sourceAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC))
                .andExpect(jsonPath("$.data.balance").value(500));

        mockMvc.perform(get("/api/balance/{account}", targetAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC))
                .andExpect(jsonPath("$.data.balance").value(1500));


        // 4. transfer back
        transferVO.setSourceAccount(targetAccount);
        transferVO.setDestinationAccount(sourceAccount);
        transferVO.setTransactionId("T002");
        mockMvc.perform(post("/api/balance/transfer")
                .content(JsonUtil.toJsonString(transferVO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC));

        // 5. init balance
        mockMvc.perform(get("/api/balance/{account}", sourceAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC))
                .andExpect(jsonPath("$.data.balance").value(1000));

        mockMvc.perform(get("/api/balance/{account}", targetAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC))
                .andExpect(jsonPath("$.data.balance").value(1000));
    }
}
