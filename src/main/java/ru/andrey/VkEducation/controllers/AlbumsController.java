package ru.andrey.VkEducation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;
import ru.andrey.VkEducation.models.album.Album;

import java.io.IOException;

@RestController
@RequestMapping("/api/albums")
public class AlbumsController {
    private final ProxyService proxyService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AlbumsController(ProxyService proxyService, ObjectMapper objectMapper) {
        this.proxyService = proxyService;
        this.objectMapper = objectMapper;
    }

    @GetMapping()
    public String getPost() throws IOException {
        return proxyService.forwardRequestGet("/albums", "albums");
    }

    @GetMapping("/{id}")
    public Album getPost(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithIdGet("/albums/" + id, "album", id);
        if (response == null || response.isEmpty()) return null;
//
        Album album = objectMapper.readValue(response, Album.class);

        return album;
    }

    @PostMapping()
    public String createPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyPost("/albums/", "album", requestBody);
    }

    @PutMapping("/{id}")
    public String putPost(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyPut("/albums/" + id, "album", id, requestBody);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyDelete("/albums/" + id, "album", id, requestBody);
    }
}
