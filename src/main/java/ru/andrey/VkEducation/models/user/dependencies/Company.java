package ru.andrey.VkEducation.models.user.dependencies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.andrey.VkEducation.models.user.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="company")
public class Company {
    @JsonIgnore
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="catchphrase")
    private String catchPhrase;
    @Column(name="bs")
    private String bs;

    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users;

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }
}
