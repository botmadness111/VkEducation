package ru.andrey.VkEducation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.models.user.User;
import ru.andrey.VkEducation.repositories.UserRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Optional<User> save(User user) {
        Optional<User> userOptional = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());

        if (userOptional.isEmpty()) {
            userRepository.save(user);
            userOptional = Optional.of(user);
        }


        return userOptional;
    }

    @Transactional
    public Optional<User> update(User userUpd) {
        Optional<User> userOptional = userRepository.findById_vk(userUpd.getId_vk());

        User user = userOptional.orElseThrow(() -> new RuntimeException("There is no such user in the database"));

        modelMapper.map(userUpd, user);

        User updatedUser = userRepository.save(user);
        return Optional.of(updatedUser);
    }

    @Transactional
    public void delete(User userDeleted){
        userRepository.deleteById_vk(userDeleted.getId_vk());
    }

    public Integer findMaxId() {
        return userRepository.findMaxId();
    }

    public Optional<User> findById_vk(int id_vk){
        return userRepository.findById_vk(id_vk);
    }
}
