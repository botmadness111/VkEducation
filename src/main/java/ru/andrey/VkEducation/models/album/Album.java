package ru.andrey.VkEducation.models.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Component;
import ru.andrey.VkEducation.models.user.User;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Album {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="id_vk")
    @JsonIgnore
    private Integer idvk;
    @Column(name="title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "my_user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Transient
    private Integer userId;
}
