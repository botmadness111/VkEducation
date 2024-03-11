package ru.andrey.VkEducation.models.user.dependencies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="geo")
public class Geo {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="lat")
    private String lat;
    @Column(name="lng")
    private String lng;

    @JsonIgnore
    @OneToOne(mappedBy = "geo", cascade = CascadeType.ALL)
    private Address address;
}
