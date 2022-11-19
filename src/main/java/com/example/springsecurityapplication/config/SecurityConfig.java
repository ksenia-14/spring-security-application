package com.example.springsecurityapplication.config;

import com.example.springsecurityapplication.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
// позволяет включить разграничение прав на определенные методы контроллера на основе аннотаций
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfiguration {
public class SecurityConfig extends WebSecurityConfigurerAdapter { // для добавления своих методов

//    // кастомная аутентификация
//    private final AuthenticationProvider authenticationProvider;
//
//    public SecurityConfig(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }
//    // настройка аутентификации
//    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
//    }

    private final PersonDetailsService personDetailsService;
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    // конфигурация Spring Security
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /*
        * csrf().disable() - отключаем защиту от межсайтовой подделки запросов
        *
        * authorizeRequests() - все страницы будут защищены процессом аутентификации
        *
        * antMatchers("/admin").hasRole("ADMIN") - страница /admin доступна пользователям
        * с ролью ADMIN ("ROLE_ADMIN" -> "ROLE_" отбрасывается)
        *
        * antMatchers("/authentication/login", "/error").permitAll() - данные страницы
        * доступны всем пользователям
        *
        * anyRequest().hasAnyRole("USER", "ADMIN") - все остальные страницы доступны для
        * пользователей с ролями USER и ADMIN
        *
        * anyRequest().authenticated() - для остальных страниц необходимо вызывать
        * метод authenticated(), который открывает форму аутентификации
        *
        * and() - переход к следующему блоку
        *
        * loginPage - на какой url адрес фильтр Spring Security будет отправлять
        * неатунтифицированного пользователя при входе на защищенную страницу
        *
        * loginProcessingUrl - на какой url будут отправляться данные с формы аутентификации
        *
        * defaultSuccessUrl - на какой url нужно направить пользователя после успешной
        * аутентификации
        *
        * failureUrl - куда нужно перейти при неверной аутентификации
        *
        * logout() - завершение сессии
        * */
        httpSecurity
//                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/authentication/login", "/authentication/registration", "/error").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/authentication/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/index", true)
                .failureUrl("/authentication/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication/login");
    }

    // настройка аутентификации
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder()); // подключение шифрования пароля
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // пароль шифровать не нужно
        return new BCryptPasswordEncoder();
    }
}
