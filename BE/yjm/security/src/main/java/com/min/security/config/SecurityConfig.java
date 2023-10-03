package com.min.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {
    public static final String ALLOWED_IP_ADDRESS = "111.111.111.111";
    public static final String SUBNET = "/32";
    public static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);
    public static final String IP_CHECK_PATH_PREFIX = "/api/temp";
    public static final String IP_CHECK_PATH_PATTERN = IP_CHECK_PATH_PREFIX + "/**";

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests((authorize) ->
                authorize
                        .requestMatchers(IP_CHECK_PATH_PATTERN).access(this::hasIpAddress)
                        .anyRequest().authenticated()
        ).build();
    }

    private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return new AuthorizationDecision(
                !(authentication.get() instanceof AnonymousAuthenticationToken)
                        && ALLOWED_IP_ADDRESS_MATCHER.matches(object.getRequest()
                ));
    }

}
