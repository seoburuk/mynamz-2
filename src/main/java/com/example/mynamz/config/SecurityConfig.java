package com.example.mynamz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.mynamz.service.CustomUserDetailsService;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // API 엔드포인트 설정
                .requestMatchers("/api/account/**").permitAll()
                // 정적 리소스 및 공개 페이지 설정
                .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
                // QR 코드 접근 경로
                .requestMatchers("/c/**").permitAll()
                .requestMatchers("/manage/c/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/find-id", "/find-password", 
                        "/find-id-proc", "/find-password-proc").permitAll()
                .requestMatchers("/manage/**").authenticated()
                .anyRequest().authenticated()
                
            )
            .formLogin(form -> form
            	    .loginPage("/login")
            	    .loginProcessingUrl("/login")
            	    .successHandler((request, response, authentication) -> {
            	        String redirectUrl = request.getParameter("redirect");
            	        String cardId = request.getParameter("cardId");
            	        
            	        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            	            if (cardId != null && !cardId.isEmpty()) {
            	                // POST 요청으로 명함 저장
            	                String saveUrl = redirectUrl + "/save";
            	                response.setContentType("text/html");
            	                response.getWriter().write(
            	                    "<form action='" + saveUrl + "' method='post' id='cardSaveForm'>" +
            	                    "<input type='hidden' name='cardId' value='" + cardId + "'>" +
            	                    "</form>" +
            	                    "<script>document.getElementById('cardSaveForm').submit();</script>"
            	                );
            	            } else {
            	                response.sendRedirect(redirectUrl);
            	            }
            	        } else {
            	            response.sendRedirect("/");
            	        }
            	    })
            	    .permitAll()
            	)
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .authenticationProvider(authenticationProvider());

        return http.build();
    }
}