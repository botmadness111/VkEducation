package ru.andrey.VkEducation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;
import ru.andrey.VkEducation.models.album.Album;
import ru.andrey.VkEducation.services.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final ProxyService proxyService;
    private final ObjectMapper objectMapper;
    private final AlbumService albumService;
    private final UserService userService;

    @Autowired
    public AlbumController(ProxyService proxyService, ObjectMapper objectMapper, AlbumService albumService, UserService userService) {
        this.proxyService = proxyService;
        this.objectMapper = objectMapper;
        this.albumService = albumService;


        this.userService = userService;
    }

    @GetMapping()
    public List<Album> createGet() throws IOException {
        List<Album> albums = new ArrayList<>();

        String response = proxyService.forwardRequestGet("/albums", "albums");

        if (response == null || response.isEmpty()) return null;

        JSONArray albumsArray = new JSONArray(response);

        for (int i = 0; i < albumsArray.length(); i++) {
            if (albumsArray.getJSONObject(i).isEmpty()) continue;
            JSONObject albumObject = albumsArray.getJSONObject(i);

            Integer userId = (Integer) albumObject.get("userId");
            Album album = objectMapper.readValue(albumObject.toString(), Album.class);
            album.setUserId(userId);

            albums.add(album);
        }

        return albums;

    }

    @GetMapping("/{id}")
    public Album createGet(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithIdGet("/albums/" + id, "album", id);

        if (response == null || response.isEmpty()) return null;

        Album album = objectMapper.readValue(response, Album.class);
        album.setIdvk(album.getId());

        if (album.getId() == null) return null;
        return album;
    }

    @PostMapping()
    public String createAlbum(@RequestBody String requestBody) throws IOException {
        String response = proxyService.forwardRequestWithBodyPost("/albums", "album", "11", requestBody);

        if (response == null || response.isEmpty()) return null;
        try {
            Album album = objectMapper.readValue(response, Album.class);
            album.setIdvk(album.getId());

            Integer userId = album.getUserId();

            Optional<Album> albumOptional = albumService.update(album);

            Album albumResponse = albumOptional.orElseThrow(() -> new RuntimeException("The album could not be added to the database"));
            albumResponse.setUserId(userId);

            return albumResponse.toString();
        } catch (RuntimeException e) {
            return e.getMessage();
        }


    }

    @PutMapping("/{id}")
    public String createPut(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        try {
            String response = proxyService.forwardRequestWithBodyPut("/albums/" + id, "album", id, requestBody);

            if (response == null || response.isEmpty()) return null;

            Album album = objectMapper.readValue(response, Album.class);
            album.setIdvk(album.getId());

            Integer userId = album.getUserId();
//            User user = userService.findById_vk(userId).get();

            Optional<Album> albumOptional = albumService.update(album);

            Album albumResponse = albumOptional.orElseThrow(() -> new RuntimeException("The album has been changed, but not added to the database"));
            albumResponse.setUserId(userId);

            return albumResponse.toString();

        } catch (RuntimeException e) {
            return "";
        }


    }

    @DeleteMapping("/{id}")
    public String createDelete(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithBodyDelete("/albums/" + id, "album", id);

        if (response == null || response.isEmpty()) return null;
        try {
            Album album = objectMapper.readValue(response, Album.class);
            album.setIdvk(album.getId());

            albumService.delete(album);

            return "Ok.";

        } catch (RuntimeException e) {
            return "The album has already been deleted from the database";
        }
    }

}
