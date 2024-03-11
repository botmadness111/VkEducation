package ru.andrey.VkEducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;

import java.io.IOException;

@RestController
public class PostController {
    private final ProxyService proxyService;

    @Autowired
    public PostController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/api/posts")
    public String getPost() throws IOException {
        return proxyService.forwardRequest("/posts");
    }

    @GetMapping("/api/posts/{id}")
    public String getPost(@PathVariable String id) throws IOException {
        return proxyService.forwardRequest("/posts/" + id);
    }

    @PostMapping("/api/posts")
    public String createPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/posts", "POST", requestBody);
    }

    @PutMapping("/api/posts")
    public String putPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/posts", "PUT", requestBody);
    }

    @DeleteMapping("/api/posts")
    public String deletePost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/posts", "DELETE", requestBody);
    }


}
