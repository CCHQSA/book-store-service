package com.epam.rd.autocode.spring.project.security.user;

import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final boolean blocked;

    public CustomUserDetails(User user) {
        this(user, false);
    }

    public CustomUserDetails(User user, boolean blocked) {
        this.user = user;
        this.blocked = blocked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user instanceof Employee) {
            return List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        }
        if (user instanceof Client) {
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
