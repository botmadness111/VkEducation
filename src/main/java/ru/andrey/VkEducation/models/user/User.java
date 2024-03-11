package ru.andrey.VkEducation.models.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import ru.andrey.VkEducation.models.album.Album;
import ru.andrey.VkEducation.models.post.Post;
import ru.andrey.VkEducation.models.user.dependencies.Address;
import ru.andrey.VkEducation.models.user.dependencies.Company;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="user_vk")
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="username")
    private String username;
    @Column(name="email")
    private String email;
    @Column(name="phone")
    private String phone;
    @Column(name="website")
    private String website;

    @ManyToOne()
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @ManyToOne()
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;



    @OneToMany(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    List<Album> albums;

    @OneToMany(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnore
    List<Post> posts;

}
