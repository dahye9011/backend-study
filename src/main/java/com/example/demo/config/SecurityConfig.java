package com.example.demo.config;

import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.jwt.JwtProvider;
import com.example.demo.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtProvider jwtProvider;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring()
                .requestMatchers("/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF disable
        http.csrf((auth) -> auth.disable());

        // Form Login disable
        http.formLogin((auth) -> auth.disable());

        // http basic disable
        http.httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/library/login", "/", "/api/library/signup", "/api/library/admin/signup", "/api/library/admin/login").permitAll()
                .requestMatchers("/api/library/book", "/api/library/book/{bookId}", "/api/library/author").permitAll() // 도서, 저자 조회
                .requestMatchers("/api/library/admin/**").hasRole("ADMIN")

                // 나머지 모든 요청 인증된 사용자만 접근 허용
                .anyRequest().authenticated());

        // 필터 추가 => 사용자 정의 필터로 로그인 과정 처리 (LoginFilter를 UsernamePasswordAuthenticationFilter 위치에서 동작하도록 설정)
        http.addFilterAt(new LoginFilter(authenticationManager(), jwtProvider), UsernamePasswordAuthenticationFilter.class);

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 뒤에 추가
        http.addFilterAfter(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        // session 설정
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
