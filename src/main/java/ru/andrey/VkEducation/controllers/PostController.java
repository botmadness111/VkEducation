package ru.andrey.VkEducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final ProxyService proxyService;

    @Autowired
    public PostController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/api/posts")
    public String getPost() throws IOException {
        return proxyService.forwardRequestGet("/posts", "posts");
    }

    @GetMapping()
    public String getPost(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithIdGet("/posts/" + id, "post", id);
        return response;
    }

    @PostMapping()
    public String createPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyPost("/posts/", "post", requestBody);
    }

    @PutMapping("/{id}")
    public String putPost(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyPut("/posts/" + id, "post", id, requestBody);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyDelete("/posts/" + id, "post", id, requestBody);
    }


}
