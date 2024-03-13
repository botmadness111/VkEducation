package ru.andrey.VkEducation.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.andrey.VkEducation.security.models.UserVk;
import ru.andrey.VkEducation.security.repositories.UserVkRepository;

@Service
public class UserVkService {
    private final UserVkRepository userVkRepository;

    @Autowired
    public UserVkService(UserVkRepository userVkRepository) {
        this.userVkRepository = userVkRepository;
    }

    public void save(UserVk userVk){
        userVkRepository.save(userVk);
    }
}
