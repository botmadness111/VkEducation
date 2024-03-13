package ru.andrey.VkEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.models.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailOrUsername(String email, String username);

    @Query("SELECT MAX(u.id) FROM User u")
    Integer findMaxId();

    @Query("SELECT u FROM User u WHERE u.id_vk = :id_vk")
    Optional<User> findById_vk(Integer id_vk);

    @Query("delete FROM User u WHERE u.id_vk = :id_vk")
    void deleteById_vk(Integer id_vk);
}
