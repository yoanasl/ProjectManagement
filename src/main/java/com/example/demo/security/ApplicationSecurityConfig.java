package com.example.demo.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.demo.enums.UserRole.ADMIN;
import static com.example.demo.enums.UserRole.USER;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig{
    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests()
                .requestMatchers("/register").permitAll()
//                .requestMatchers("/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/projects").hasRole(USER.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic().and().csrf().disable();
        return http.build();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }
}
