package com.yash.FinanceTracker.config;


import com.yash.FinanceTracker.filter.JwtFilter;
import com.yash.FinanceTracker.oauth.OAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private OAuthSuccessHandler handler;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        return httpSecurity
                .authorizeHttpRequests(request->
                {
                    request
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth->
                {
//                    oauth.Customizer.withDefaults();
                    oauth.successHandler(handler);
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
