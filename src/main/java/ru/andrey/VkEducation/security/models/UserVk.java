package ru.andrey.VkEducation.security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.andrey.VkEducation.audit.models.Audit;

import java.util.List;

@Entity
@Table(name = "security_user_vk")
@Getter
@Setter
@NoArgsConstructor
public class UserVk {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 30)
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "userVk")
    private List<Audit> auditList;

}
