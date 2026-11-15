package com.epam.rd.autocode.spring.project.conf;

import com.epam.rd.autocode.spring.project.security.CustomUserDetailsService;
import com.epam.rd.autocode.spring.project.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/h2-console/**",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .requestMatchers(
                                "/books/add",
                                "/books/{name}/edit",
                                "/books/{name}/delete",
                                "/orders",
                                "/orders/{id}/confirm",
                                "/clients",
                                "/clients/{email}/block",
                                "/clients/{email}/unblock"
                        ).hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/profile/delete").permitAll()
                        .requestMatchers("/profile/delete").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/basket/add").permitAll()
                        .requestMatchers(
                                "/basket",
                                "/basket/add",
                                "/orders/submit",
                                "/orders/my"
                        ).hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET,
                                "/home",
                                "/books",
                                "/books/{name}",
                                "/profile"
                        ).hasAnyRole("CLIENT", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST,
                                "/profile/edit"
                        ).hasAnyRole("CLIENT", "EMPLOYEE")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "jwt")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
