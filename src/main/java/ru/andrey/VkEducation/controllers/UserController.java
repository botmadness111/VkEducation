package ru.andrey.VkEducation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.andrey.VkEducation.controllers.Proxy.ProxyService;
import ru.andrey.VkEducation.models.user.User;
import ru.andrey.VkEducation.models.user.dependencies.Address;
import ru.andrey.VkEducation.models.user.dependencies.Company;
import ru.andrey.VkEducation.models.user.dependencies.Geo;
import ru.andrey.VkEducation.services.dataBase.AddressService;
import ru.andrey.VkEducation.services.dataBase.CompanyService;
import ru.andrey.VkEducation.services.dataBase.GeoService;
import ru.andrey.VkEducation.services.dataBase.UserService;

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
        return proxyService.forwardRequest("/posts");
    }

    @GetMapping("/{id}")
    public User getPost(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequest("/users/" + id);
        User user = objectMapper.readValue(response, User.class);

        Company company = user.getCompany();
        Address address = user.getAddress();
        Geo geo = address.getGeo();


        geoService.save(geo);
        addressService.save(address);
        companyService.save(company);

        userService.save(user);

        return user;
    }

    @PostMapping("")
    public String createPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/posts", "POST", requestBody);
    }

    @PutMapping("")
    public String putPost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/posts", "PUT", requestBody);
    }

    @DeleteMapping("")
    public String deletePost(@RequestBody String requestBody) throws IOException {
        return proxyService.forwardRequestWithBody("/posts", "DELETE", requestBody);
    }

}
