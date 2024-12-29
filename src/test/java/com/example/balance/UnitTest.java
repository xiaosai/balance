package com.example.balance;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.balance.common.Constants;

/**
 * @author zhangsaiyong <zhangsaiyong@kuaishou.com>
 * Created on 2024-12-29
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetBalance_shouldSuccess() throws Exception {
        String sourceAccount = "ACCOUNT_101";

        mockMvc.perform(get("/api/balance/{account}", sourceAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_SUC))
                .andExpect(jsonPath("$.data.balance").value(1000));
    }

    @Test
    void whenGetBalanceWrongAccount_shouldFailed() throws Exception {
        String sourceAccount = "ACCOUNT_101_ERROR";

        mockMvc.perform(get("/api/balance/{account}", sourceAccount))
                .andExpect(jsonPath("$.code").value(Constants.RESPONSE_CODE_ACCOUNT_NOT_EXIST))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message", containsString("account not exist")))
                ;
    }
}
