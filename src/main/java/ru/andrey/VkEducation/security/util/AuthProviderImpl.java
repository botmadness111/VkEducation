package ru.andrey.VkEducation.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.andrey.VkEducation.security.services.UserVkDetailsService;


@Component
public class AuthProviderImpl implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;

    private final UserVkDetailsService userVkDetailsService;

    @Autowired
    public AuthProviderImpl(PasswordEncoder passwordEncoder,  UserVkDetailsService userVkDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userVkDetailsService = userVkDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();


        UserVkDetails userVkDetails = userVkDetailsService.loadUserByUsername(name);

        if (!passwordEncoder.matches(password, userVkDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password!");
        }
        return new UsernamePasswordAuthenticationToken(userVkDetails, password, userVkDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
