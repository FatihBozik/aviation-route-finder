package com.fatihbozik.aviationroutefinder.security;

import com.fatihbozik.aviationroutefinder.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public record CustomerUserDetails(UserEntity delegate) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return delegate.getEnabled();
    }
}
