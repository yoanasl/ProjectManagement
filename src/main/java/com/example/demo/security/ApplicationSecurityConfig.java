package com.example.demo.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.Collections;

import static com.example.demo.enums.UserRole.*;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig{
    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private DataSource dataSource;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests()
                .requestMatchers("/register").permitAll()
                .requestMatchers("/login").permitAll()
//                .requestMatchers("/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/projects").hasRole(USER.name())
                .anyRequest()
                .authenticated()
                .and()
                /*.formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .permitAll()
                .and()*/
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
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

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?")
                .passwordEncoder(passwordEncoder)
                .and()
                .userDetailsService(userDetailsService);

        // Add manual users
        auth
                .inMemoryAuthentication()
                .withUser("user1").password("123456").roles("ADMIN")
                .and()
                .withUser("user2").password("123456").roles("PROJECT_MANAGER");
    }*/
}
