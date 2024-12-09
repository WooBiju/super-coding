package com.github.supercoding.config.security;

import com.github.supercoding.web.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))  // frameOptions 동일 출처 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션을 무상태로 설정
                .httpBasic(httpBasic -> httpBasic.disable())  // HTTP Basic 인증 비활성화
                .formLogin(formLogin -> formLogin.disable())  // Form 로그인 비활성화
                .rememberMe(rememberMe -> rememberMe.disable())  // Remember-Me 비활성화
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/resources/static/**", "/v1/api/sign/*").permitAll()  // 누구나 접근 가능
                        .requestMatchers("/v1/api/air-reservation/*").hasRole("USER")  // "USER" 권한을 가진 사용자만 접근
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((new CustomAuthenticationEntryPoint())  // 인증 실패 시 처리
                        .accessDeniedHandler(new CustomerAccessDeniedHandler())  // 권한 부족 시 처리
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)  // JWT 필터 추가

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
