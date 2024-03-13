package ru.andrey.VkEducation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;
import ru.andrey.VkEducation.models.post.Post;
import ru.andrey.VkEducation.models.user.User;
import ru.andrey.VkEducation.services.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final ProxyService proxyService;
    private final ObjectMapper objectMapper;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(ProxyService proxyService, ObjectMapper objectMapper, PostService postService, UserService userService) {
        this.proxyService = proxyService;
        this.objectMapper = objectMapper;
        this.postService = postService;


        this.userService = userService;
    }

    @GetMapping()
    public List<Post> createGet() throws IOException {
        List<Post> posts = new ArrayList<>();

        String response = proxyService.forwardRequestGet("/posts", "posts");

        if (response == null || response.isEmpty()) return null;

        JSONArray postsArray = new JSONArray(response);

        for (int i = 0; i < postsArray.length(); i++) {
            if (postsArray.getJSONObject(i).isEmpty()) continue;
            JSONObject postObject = postsArray.getJSONObject(i);

            Integer userId = (Integer) postObject.get("userId");
            Post post = objectMapper.readValue(postObject.toString(), Post.class);
            post.setUserId(userId);

            posts.add(post);
        }

        return posts;

    }

    @GetMapping("/{id}")
    public Post createGet(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithIdGet("/posts/" + id, "post", id);

        if (response == null || response.isEmpty()) return null;

        Post post = objectMapper.readValue(response, Post.class);
        post.setIdvk(post.getId());

        if (post.getId() == null) return null;
        return post;
    }

    @PostMapping()
    public String createPost(@RequestBody String requestBody) throws IOException {
        String response = proxyService.forwardRequestWithBodyPost("/posts", "post", "11", requestBody);

        if (response == null || response.isEmpty()) return null;
        try {
            Post post = objectMapper.readValue(response, Post.class);
            post.setIdvk(post.getId());

            Integer userId = post.getUserId();

            Optional<Post> postOptional = postService.update(post);

            Post postResponse = postOptional.orElseThrow(() -> new RuntimeException("The post could not be added to the database"));
            postResponse.setUserId(userId);

            return postResponse.toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }


    }

    @PutMapping("/{id}")
    public String createPut(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        try {
            String response = proxyService.forwardRequestWithBodyPut("/posts/" + id, "post", id, requestBody);

            if (response == null || response.isEmpty()) return null;

            Post post = objectMapper.readValue(response, Post.class);
            post.setIdvk(post.getId());

            Integer userId = post.getUserId();
//            User user = userService.findById_vk(userId).get();

            Optional<Post> postOptional = postService.update(post);

            Post postResponse = postOptional.orElseThrow(() -> new RuntimeException("The post has been changed, but not added to the database"));
            postResponse.setUserId(userId);

            return postResponse.toString();

        } catch (RuntimeException e) {
            return "";
        }


    }

    @DeleteMapping("/{id}")
    public String createDelete(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithBodyDelete("/posts/" + id, "post", id);

        if (response == null || response.isEmpty()) return null;
        try {
            Post post = objectMapper.readValue(response, Post.class);
            post.setIdvk(post.getId());

            postService.delete(post);

            return "Ok.";

        } catch (RuntimeException e) {
            return "The post has already been deleted from the database";
        }
    }

}
