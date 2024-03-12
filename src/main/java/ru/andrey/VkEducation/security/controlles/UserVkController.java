package ru.andrey.VkEducation.security.controlles;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.security.models.UserVk;
import ru.andrey.VkEducation.security.services.RegistrationService;
import ru.andrey.VkEducation.security.services.UserVkDetailsService;
import ru.andrey.VkEducation.security.util.AuthProviderImpl;
import ru.andrey.VkEducation.security.util.UserVkDetails;

@RestController
@RequestMapping("/userVk")
public class UserVkController {
    private final RegistrationService registrationService;
    private final UserVkDetailsService userVkDetailsService;
    private final AuthProviderImpl authProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserVkController(RegistrationService registrationService, UserVkDetailsService userVkDetailsService, AuthProviderImpl authProvider, PasswordEncoder passwordEncoder) {
        this.registrationService = registrationService;
        this.userVkDetailsService = userVkDetailsService;
        this.authProvider = authProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody UserVk userVk) {

        registrationService.register(userVk);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody UserVk userVk) {

        String name = userVk.getUsername();
        String password = userVk.getPassword().toString();

        UserVkDetails userVkDetails = userVkDetailsService.loadUserByUsername(name);

        if (!passwordEncoder.matches(password, userVkDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password!");
        }

        authProvider.authenticate(new UsernamePasswordAuthenticationToken(name, password));

        return new ResponseEntity<>("Good!", HttpStatus.OK);

    }
}
