package com.onixbyte.oauth.config;

import com.onixbyte.oauth.authentication.provider.MsalAuthenticationProvider;
import com.onixbyte.oauth.authentication.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        return httpSecurity
                .cors((customiser) -> customiser
                        .configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((customiser) -> customiser
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((customiser) -> customiser
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/authorisation/**").permitAll()
                        .requestMatchers("/authorisation/logout").authenticated()
                        .anyRequest().authenticated())
                // .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity httpSecurity,
            UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider,
            MsalAuthenticationProvider msalAuthenticationProvider
    ) throws Exception {
        var authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(usernamePasswordAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(msalAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }
}
