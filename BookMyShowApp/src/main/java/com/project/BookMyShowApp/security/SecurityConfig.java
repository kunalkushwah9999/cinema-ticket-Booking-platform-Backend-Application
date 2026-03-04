package com.project.BookMyShowApp.security;

import com.project.BookMyShowApp.jwt.JwtAuthenticationFilter;
import com.project.BookMyShowApp.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService){
        this.customUserDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        //Public
                        .requestMatchers("/api/user/register", "/api/user/login").permitAll()
                        .requestMatchers("/api/movie", "/api/movie/language/{language}", "/api/movie/genre/{genre}", "/api/movie/title/{title}").permitAll()
                        .requestMatchers("/api/show","/api/show/movie/**", "/api/show/movie/{movieId}/city/{city}", "/api/show/city/{city}/date/{date}").permitAll()
                        .requestMatchers("/api/theater/city/{city}").permitAll()

                        //User
                        .requestMatchers("/api/user/delete/{id}").hasRole("USER")
                        .requestMatchers("/api/booking/create", "/api/booking/delete/{id}").hasRole("USER")

                        //Admin
                        .requestMatchers("/api/theater/create", "/api/theater/update/{id}", "/api/theater/delete/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/movie/create", "/api/movie/delete/{id}", "/api/movie/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/user/admin/create-theater-admin").hasRole("ADMIN")

                        //TheaterAdmin
                        .requestMatchers("/api/show/create", "/api/show/update/{id}", "/api/show/delete/{id}").hasRole("THEATER_ADMIN")

                        .anyRequest().authenticated()

                );
        //.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        AuthenticationManager authenticationManager = config.getAuthenticationManager();
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
        return cryptPasswordEncoder;
    }

}