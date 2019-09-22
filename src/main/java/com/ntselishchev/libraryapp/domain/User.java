package com.ntselishchev.libraryapp.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Document(value = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @Field("userName")
    private String userName;

    @Field("password")
    private String password;

    @Field("expired")
    private boolean expired;

    @Field("locked")
    private boolean locked;

    @Field("credentialsExpired")
    private boolean credentialsExpired;

    @Field("enabled")
    private boolean enabled;

    @Field("role")
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> role.toUpperCase());
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}