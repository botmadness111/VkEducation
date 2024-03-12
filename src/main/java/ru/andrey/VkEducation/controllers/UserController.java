package ru.andrey.VkEducation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;
import ru.andrey.VkEducation.models.user.User;
import ru.andrey.VkEducation.models.user.dependencies.Address;
import ru.andrey.VkEducation.models.user.dependencies.Company;
import ru.andrey.VkEducation.models.user.dependencies.Geo;
import ru.andrey.VkEducation.services.AddressService;
import ru.andrey.VkEducation.services.CompanyService;
import ru.andrey.VkEducation.services.GeoService;
import ru.andrey.VkEducation.services.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final ProxyService proxyService;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final CompanyService companyService;
    private final AddressService addressService;
    private final GeoService geoService;

    @Autowired
    public UserController(ProxyService proxyService, ObjectMapper objectMapper, UserService userService, CompanyService companyService, AddressService addressService, GeoService geoService) {
        this.proxyService = proxyService;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.companyService = companyService;
        this.addressService = addressService;
        this.geoService = geoService;
    }

    @GetMapping()
    public String getPost() throws IOException {
        return proxyService.forwardRequestGet("/users", "users");
    }

    @GetMapping("/{id}")
    public User getPost(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithIdGet("/users/" + id, "user", id);

        if (response == null || response.isEmpty()) return null;
//
        User user = objectMapper.readValue(response, User.class);
//
//        Company company = user.getCompany();
//        Address address = user.getAddress();
//        Geo geo = address.getGeo();
//
//        geoService.save(geo);
//        addressService.save(address);
//        companyService.save(company);
//
//        userService.save(user);

        return user;
    }

    @PostMapping()
    public String createPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyPost("/users/", "user", requestBody);
    }

    @PutMapping("/{id}")
    public String putPost(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyPut("/users/" + id, "user", id, requestBody);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable String id, @RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBodyDelete("/users/" + id, "user", id, requestBody);
    }

}
