package ru.andrey.VkEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.models.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
