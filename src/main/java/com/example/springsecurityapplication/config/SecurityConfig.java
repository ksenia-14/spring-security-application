package com.example.springsecurityapplication.config;

import com.example.springsecurityapplication.responses.Response;
import com.example.springsecurityapplication.services.PersonDetailsService;
import com.example.springsecurityapplication.token.JWTAuthenticationFilter;
import com.example.springsecurityapplication.token.JWTTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;
    private final JWTTokenHelper jWTTokenHelper;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(PersonDetailsService personDetailsService, JWTTokenHelper jWTTokenHelper, AuthenticationEntryPoint authenticationEntryPoint) {
        this.personDetailsService = personDetailsService;
        this.jWTTokenHelper = jWTTokenHelper;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    // конфигурация Spring Security
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable().cors().and().headers().frameOptions().disable()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint).and()
            .authorizeRequests((request) -> request
                    .antMatchers( "/api/authentication/login","/api/authentication/registration", "/api/product/**", "/api/file/**").permitAll()
                    .antMatchers("/api/admin/**").hasRole("ADMIN")
                    .antMatchers("/api/user/**").hasRole("USER")
                    .antMatchers("/api/seller/**").hasRole("SELLER")
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated())
            .addFilterBefore(new JWTAuthenticationFilter(personDetailsService, jWTTokenHelper),
                             UsernamePasswordAuthenticationFilter.class)
            .formLogin().loginPage("/api/authentication/login")
            .loginProcessingUrl("/process_login")
            .defaultSuccessUrl("/index", true)
            .failureUrl("/api/login")
            .and()
            .logout().logoutUrl("/api/authentication/logout").logoutSuccessUrl("/api/authentication/login")
            ;
            // РАЗРЕШАЕМ ВСЕЕЕЕ НИКАКОГО CORS
            http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

    }

    /*
    *         http
            .csrf().disable().cors().and().headers().frameOptions().disable()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint).and()
            .authorizeRequests((request) -> request.antMatchers( "/api/authentication/login","/api/authentication/registration").permitAll()
            .antMatchers("/api/admin/**").hasRole("ADMIN")
            .antMatchers("/api/user/**").hasRole("USER")
            .antMatchers("/api/seller/**").hasRole("SELLER")
            .anyRequest().hasAnyRole("USER", "ADMIN", "SELLER")
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated())
            .addFilterBefore(new JWTAuthenticationFilter(personDetailsService, jWTTokenHelper),
                             UsernamePasswordAuthenticationFilter.class)
            .formLogin().loginPage("/api/authentication/login")
            .loginProcessingUrl("/process_login")
            .defaultSuccessUrl("/index", true)
            .failureUrl("/api/login")
            .and()
            .logout().logoutUrl("/api/authentication/logout").logoutSuccessUrl("/api/authentication/login")
            ;*/

    // настройка аутентификации
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder()); // подключение шифрования пароля
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
