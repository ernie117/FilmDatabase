package com.practice.work.films.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("#{@environment.getProperty('spring.profiles') ?: 'dev'}")
    private String env;

    @Value("#{@environment.getProperty('spring.security.user.name') ?: 'unknown_user'}")
    private String user;

    @Value("#{@environment.getProperty('spring.security.user.password') ?: 'unknown_pw'}")
    private String password;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (env.equalsIgnoreCase("prod")) {
            auth.inMemoryAuthentication()
                    .passwordEncoder(new Argon2PasswordEncoder())
                    .withUser(user)
                    .password(String.format("{argon2}%s", password))
                    .roles("ADMIN", "USER");
        }
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (env.equalsIgnoreCase("prod")) {
            httpSecurity
                    .csrf().disable()
                    // .requiresChannel() When HTTPS is actually enabled on EC2
                    // .anyRequest()
                    // .requiresSecure()
                    // .and()
                    .authorizeRequests()
                    .anyRequest()
                    .hasRole("ADMIN")
                    .and()
                    .httpBasic();
        } else {
            httpSecurity.csrf().disable();
        }
    }

//     Also for when HTTPS is properly enabled, redirects HTTP to HTTPS
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http.redirectToHttps();
//        return http.build();
//    }
}
