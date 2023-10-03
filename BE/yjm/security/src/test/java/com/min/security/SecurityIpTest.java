package com.min.security;

import com.min.security.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.min.security.config.SecurityConfig.ALLOWED_IP_ADDRESS;
import static com.min.security.config.SecurityConfig.IP_CHECK_PATH_PREFIX;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebAppConfiguration
@WithMockUser
public class SecurityIpTest {

    public static final String NOT_ALLOWED_ID_ADDRESS = "11.11.11.11";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void IP_CHECK_PATH_외에는_ip_검사를_하지않는다() throws Exception {
        mvc.perform(get("/"))
                .andExpect(allowed());
    }

    @Test
    void IP_CHECK_PATH_접근시_ip_검사를_해야한다() throws Exception {
        mvc.perform(get(IP_CHECK_PATH_PREFIX))
                .andExpect(notAllowed());
    }

    @Test
    void IP_CHECK_PATH_접근시_허용된_ip_가_아니면_권한이_없어야한다() throws Exception {
        mvc.perform(get(IP_CHECK_PATH_PREFIX).with(remoteAddr(NOT_ALLOWED_ID_ADDRESS)))
                .andExpect(notAllowed());
    }

    @Test
    void IP_CHECK_PATH_접근시_허용된_ip_와_인증된_유저는_통과해야한다() throws Exception {
        mvc.perform(get(IP_CHECK_PATH_PREFIX).with(remoteAddr(ALLOWED_IP_ADDRESS)))
                .andExpect(allowed());
    }

    @Test
    @WithAnonymousUser
    void IP_CHECK_PATH_접근시_허용된_ip_지만_인증되지_않은_유저는_권한이_없어야한다() throws Exception {
        mvc.perform(get(IP_CHECK_PATH_PREFIX).with(remoteAddr(ALLOWED_IP_ADDRESS)))
                .andExpect(notAllowed());

    }

    private static ResultMatcher allowed() {
        return status().isNotFound();
    }

    private static ResultMatcher notAllowed() {
        return status().isForbidden();
    }

    private static RequestPostProcessor remoteAddr(final String remoteHost) {
        return request -> {
            request.setRemoteAddr(remoteHost);
            return request;
        };
    }

}
