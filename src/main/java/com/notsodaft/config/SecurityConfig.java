package com.notsodaft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.notsodaft.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.requestMatchers("/", "/register", "/login",
                             "/css/**", "/js/**", "/uploads/**",
                             "/reviews/*/comments",
                             "/forgot-password", "/reset-password",
                             "/proof/**").permitAll().requestMatchers("/admin/**").hasRole("ADMIN") .anyRequest().authenticated()).formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll()).logout(logout -> logout.logoutSuccessUrl("/").permitAll());
    return http.build();
}

    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}