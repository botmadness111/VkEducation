package ru.andrey.VkEducation.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.models.post.Post;
import ru.andrey.VkEducation.models.user.User;
import ru.andrey.VkEducation.repositories.PostRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostService(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Optional<Post> save(Post post) {
        Optional<Post> postOptional = postRepository.findByIdvk(post.getIdvk());

        if (postOptional.isEmpty()) {
            postRepository.save(post);
            postOptional = Optional.of(post);
        }

        return postOptional;
    }

    @Transactional
    public Optional<Post> update(Post postUpd) {
        Optional<Post> postOptional = postRepository.findByIdvk(postUpd.getIdvk());

        Post post = postOptional.orElseThrow(() -> new RuntimeException("There is no such post in the database"));

        modelMapper.map(postUpd, post);

        Post updatedPost = postRepository.save(post);
        return Optional.of(updatedPost);
    }

    @Transactional
    public void delete(Post postDeleted) {
        postRepository.deleteByIdvk(postDeleted.getIdvk());
    }

}
