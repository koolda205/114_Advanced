package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class Role implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return null;
    }

}
