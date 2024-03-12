package ru.andrey.VkEducation.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.security.models.UserVk;
import ru.andrey.VkEducation.security.repositories.UserVkRepository;
import ru.andrey.VkEducation.security.util.UserVkDetails;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserVkDetailsService implements UserDetailsService {

    private final UserVkRepository userVkRepository;

    @Autowired
    public UserVkDetailsService(UserVkRepository userVkRepository) {
        this.userVkRepository = userVkRepository;
    }

    @Override
    public UserVkDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserVk> userVk = userVkRepository.findByUsername(username);

        return userVk.map(UserVkDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

    }
}
