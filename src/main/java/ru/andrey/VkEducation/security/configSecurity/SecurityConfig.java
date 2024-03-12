package ru.andrey.VkEducation.security.configSecurity;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.andrey.VkEducation.security.services.UserVkDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    private final UserVkDetailsService userVkDetailsService;

    @Autowired
    public SecurityConfig(UserVkDetailsService userVkDetailsService) {
        this.userVkDetailsService = userVkDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/userVk/registration", "/userVk/auth").permitAll()
//                        .requestMatchers("/api/posts/**").hasAnyAuthority("ROLE_POSTS", "ROLE_ADMIN")
//                        .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_USERS", "ROLE_ADMIN")
//                        .requestMatchers("/api/albums/**").hasAnyAuthority("ROLE_ALBUMS", "ROLE_ADMIN")
//                        .anyRequest().authenticated()

                )
                .formLogin(withDefaults())  // перекинет на стандартную странциу регистрации
                .csrf(AbstractHttpConfigurer::disable); //для работы post/put/delete / защита от взломала...
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

//        UserDetails users = User.builder().username("users").password("users").roles("USER").build();
//        UserDetails admin = User.builder().username("admin").password("admin").roles("ADMIN").build();
//        UserDetails posts = User.builder().username("posts").password("posts").roles("POST").build();
//        UserDetails albums = User.builder().username("albums").password("albums").roles("ALBUM").build();
//
//        return new InMemoryUserDetailsManager(users, admin, posts, albums);
        return userVkDetailsService;
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
