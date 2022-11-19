package com.example.springsecurityapplication.security;

import com.example.springsecurityapplication.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    private final Person person;
    @Autowired
    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // возвращает лист из одного элемента
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    // аккаунт действителен
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // аккаунт не заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // пароль действителен
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // аккаунт активен
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Person getPerson() {
        return this.person;
    }
}
