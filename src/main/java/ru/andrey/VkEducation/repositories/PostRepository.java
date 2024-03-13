package ru.andrey.VkEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.models.post.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {


    Optional<Post> findByIdvk(Integer id_vk);

    void deleteByIdvk(Integer id_vk);
}
