package com.example.springpr.gymapp.configTest;

import com.example.springpr.gymapp.GymApplication;
import com.example.springpr.gymapp.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessPublicEndpoint_withoutAuth_isAllowed() throws Exception {
        mockMvc.perform(get("/welcome"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    void accessTrainerEndpoint_withTraineeRole_isForbidden() throws Exception {
        mockMvc.perform(get("/trainer/myProfile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void accessAnySecuredEndpoint_withoutProperRole_isUnauthorized() throws Exception {
        mockMvc.perform(get("/trainee/myProfile"))
                .andExpect(status().isForbidden());
    }
}
