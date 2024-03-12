package ru.andrey.VkEducation.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.security.models.UserVk;
import ru.andrey.VkEducation.security.repositories.UserVkRepository;

@Service
@Transactional(readOnly = true)
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;

    private final UserVkRepository userVkRepository;

    @Autowired
    public RegistrationService(PasswordEncoder passwordEncoder, UserVkRepository userVkRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userVkRepository = userVkRepository;
    }

    @Transactional
    public void register(UserVk userVk) {
        String encodedPassword = passwordEncoder.encode(userVk.getPassword());
        userVk.setPassword(encodedPassword);

        userVkRepository.save(userVk);
    }
}
