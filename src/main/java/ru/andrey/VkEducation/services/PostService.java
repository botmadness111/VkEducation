package ru.andrey.VkEducation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.models.post.Post;
import ru.andrey.VkEducation.repositories.PostRepository;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }
}
