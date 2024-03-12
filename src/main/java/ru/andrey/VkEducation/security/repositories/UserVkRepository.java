package ru.andrey.VkEducation.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.security.models.UserVk;

import java.util.Optional;

@Repository
public interface UserVkRepository extends JpaRepository<UserVk, Integer> {
    Optional<UserVk> findByUsername(String username);
}
