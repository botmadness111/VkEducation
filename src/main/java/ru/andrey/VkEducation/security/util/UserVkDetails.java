package ru.andrey.VkEducation.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.andrey.VkEducation.security.models.UserVk;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserVkDetails implements UserDetails {
    private final UserVk userVk;

    public UserVkDetails(UserVk userVk) {
        this.userVk = userVk;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(userVk.getRole().split(", "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userVk.getPassword();
    }

    @Override
    public String getUsername() {
        return userVk.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
