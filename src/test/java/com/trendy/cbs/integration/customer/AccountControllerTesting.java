package com.trendy.cbs.integration.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendy.cbs.payload.request.CreateSelfAccountReq;
import com.trendy.cbs.repos.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AccountControllerTesting {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createAccount_shouldReturn201() throws Exception {

        // Arrange
        CreateSelfAccountReq request = new CreateSelfAccountReq();
        request.setAccountTypeId(1L);
        request.setCurrencyId(1L);

        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "none")
                .claim("sub", "customer123")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/customer/accounts")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.accountNumber").exists());

        assertEquals(1, accountRepository.count());
    }
}
