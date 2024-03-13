package ru.andrey.VkEducation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<User> createGet() throws IOException {
        List<User> users = new ArrayList<>();

        String response = proxyService.forwardRequestGet("/users", "users");

        if (response == null || response.isEmpty()) return null;

        JSONArray usersArray = new JSONArray(response);
        for (int i = 0; i < usersArray.length(); i++) {
            if (usersArray.getJSONObject(i).isEmpty()) continue;
            JSONObject userObject = usersArray.getJSONObject(i);

            User user = objectMapper.readValue(userObject.toString(), User.class);
            user.setId_vk(user.getId());
            users.add(user);

//            String jsonUser = objectMapper.writeValueAsString(user);
//            createPost(user.getId_vk().toString(), jsonUser);

        }

        return users;

    }

    @GetMapping("/{id}")
    public User createGet(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithIdGet("/users/" + id, "user", id);

        if (response == null || response.isEmpty()) return null;

        User user = objectMapper.readValue(response, User.class);
        user.setId_vk(user.getId());

        if (user.getId() == null) return null;

        return user;
    }

    @PostMapping()
    public String createPost(@RequestParam(required = false) String id, @RequestBody String requestBody) throws IOException {
        if (id == null) id = "11"; // по стандарту регестрируется 11 id...
        String response = proxyService.forwardRequestWithBodyPost("/users", "user", id, requestBody);

        if (response == null || response.isEmpty()) return null;
        try {
            User user = objectMapper.readValue(response, User.class);
            user.setId_vk(user.getId());

            Company company = user.getCompany();
            Address address = user.getAddress();
            Geo geo = address.getGeo();

            Optional<Geo> geoOptional = geoService.save(geo);
            address.setGeo(geoOptional.orElseThrow(() -> new RuntimeException("The geo could not be added to the database"))); //хорошей практиктой будет, если будем следить за двусторонней связью для кэша

            Optional<Address> addressOptional = addressService.save(address);
            user.setAddress(addressOptional.orElseThrow(() -> new RuntimeException("The address could not be added to the database"))); //хорошей практиктой будет, если будем следить за двусторонней связью для кэша

            Optional<Company> companyOptional = companyService.save(company);
            user.setCompany(companyOptional.orElseThrow(() -> new RuntimeException("The company could not be added to the database"))); //хорошей практиктой будет, если будем следить за двусторонней связью для кэша

            Optional<User> userOptional = userService.save(user);


            User userResponse = userOptional.orElseThrow(() -> new RuntimeException("The user could not be added to the database"));

            return objectMapper.writeValueAsString(userResponse);
        } catch (RuntimeException e) {
            return e.getMessage();
        }


    }

    @PutMapping("/{id}")
    public String createPut(@PathVariable String id, @RequestBody String requestBody) throws IOException {

        String response = proxyService.forwardRequestWithBodyPut("/users/" + id, "user", id, requestBody);

        if (response == null || response.isEmpty()) return null;
        try {
            User user = objectMapper.readValue(response, User.class);
            user.setId_vk(user.getId());

            Company company = user.getCompany();
            Address address = user.getAddress();
            Geo geo = address.getGeo();


            Optional<Geo> geoOptional = geoService.save(geo);
            address.setGeo(geoOptional.orElseThrow(() -> new RuntimeException("The geo has been changed, but not added to the database"))); //хорошей практиктой будет, если будем следить за двусторонней связью для кэша

            Optional<Address> addressOptional = addressService.save(address);
            user.setAddress(addressOptional.orElseThrow(() -> new RuntimeException("The address has been changed, but not added to the database"))); //хорошей практиктой будет, если будем следить за двусторонней связью для кэша

            Optional<Company> companyOptional = companyService.save(company);
            user.setCompany(companyOptional.orElseThrow(() -> new RuntimeException("The company has been changed, but not added to the database"))); //хорошей практиктой будет, если будем следить за двусторонней связью для кэша

            Optional<User> userOptional = userService.update(user);


            User userResponse = userOptional.orElseThrow(() -> new RuntimeException("The user has been changed, but not added to the database"));
            return userResponse.toString();

        } catch (RuntimeException e) {
            return e.getMessage();
        }


    }

    @DeleteMapping("/{id}")
    public String createDelete(@PathVariable String id) throws IOException {
        String response = proxyService.forwardRequestWithBodyDelete("/users/" + id, "user", id);

        if (response == null || response.isEmpty()) return null;
        try {
            User user = objectMapper.readValue(response, User.class);
            user.setId_vk(user.getId());

            userService.delete(user);

            return "Ok.";

        } catch (RuntimeException e) {
            return "The user has already been deleted from the database";
        }
    }

}
