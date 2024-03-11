package ru.andrey.VkEducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;

import java.io.IOException;

@RestController
public class AlbumsController {
    private final ProxyService proxyService;

    @Autowired
    public AlbumsController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/api/albums")
    public String getPost() throws IOException {
        return proxyService.forwardRequest("/albums");
    }

    @GetMapping("/api/albums/{id}")
    public String getPost(@PathVariable String id) throws IOException {
        return proxyService.forwardRequest("/albums/" + id);
    }

    @PostMapping("/api/albums")
    public String createPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/albums", "POST", requestBody);
    }

    @PutMapping("/api/albums")
    public String putPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/albums", "PUT", requestBody);
    }

    @DeleteMapping("/api/albums")
    public String deletePost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/albums", "DELETE", requestBody);
    }
}
