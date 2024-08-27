package com.dr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain sfc(HttpSecurity http)throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)

                /*.sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))*/

                .authorizeHttpRequests(authorizeRequests-> authorizeRequests

                 // HR endpoints
                .requestMatchers("/hrHome" ,"/addEmployee", "/getIdForUpdate", "/updateEmployee",
                              "/getAllEmployees", "/getIdForSingleEmp", "/getSingleEmp", "/getAppliedLeaves",
                              "/approveLeave/", "/rejectLeave/", "/getResignRequests", "/approveResign/",
                              "/rejectResign/").hasAuthority("ROLE_HR")


                // Employee endpoints
                .requestMatchers("/empHome", "/markAttendance/{id}", "/addCheckIn/{id}",
                        "/addCheckOut/{id}", "/applyLeave/{id}", "/applyResign/{id}", "/viewSelfDetails/{id}",
                        "/leaveStatus/{id}", "/resignStatus/{id}").hasAuthority("ROLE_EMPLOYEE")


                // Public endpoints
                        .requestMatchers("/loginForToken").permitAll()
                .requestMatchers("/", "/error", "/login", "/denied", "/hrWelcome", "/tempLogin",
                        "/resetPwd", "/empWelcome", "/v3/api-docs", "/swagger-ui/index.html").permitAll()


                 // All other requests need to be authenticated
                .anyRequest().authenticated()
        )
                .formLogin(fl->fl
                        .loginPage("/login")
                        .permitAll()
                )
                .exceptionHandling(eh->eh
                        .accessDeniedPage("/denied")
                );
        return http.build();
    }
}

//****************************************** JWT Token *************************************************//
