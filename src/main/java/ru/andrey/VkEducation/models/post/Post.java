package ru.andrey.VkEducation.models.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.andrey.VkEducation.models.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="post")
public class Post {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    @Column(name="id_vk")
    private Integer idvk;
    @Column(name="title")
    private String title;
    @Column(name="body")
    private String body;



    @ManyToOne()
    @JoinColumn(name = "my_user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Transient
    private Integer userId;
}
