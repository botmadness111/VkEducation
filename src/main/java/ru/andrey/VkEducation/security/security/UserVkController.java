package ru.andrey.VkEducation.security.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.security.models.UserVk;
import ru.andrey.VkEducation.security.services.RegistrationService;

@RestController
@RequestMapping("/userVk")
public class UserVkController {
    private final RegistrationService registrationService;

    @Autowired
    public UserVkController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody UserVk userVk) {

        registrationService.register(userVk);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
