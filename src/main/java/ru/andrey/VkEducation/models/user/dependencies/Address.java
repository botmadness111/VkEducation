package ru.andrey.VkEducation.models.user.dependencies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Component;
import ru.andrey.VkEducation.models.user.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="address")
public class Address {
    @JsonIgnore
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="street")
    private String street;
    @Column(name="suite")
    private String suite;
    @Column(name="city")
    private String city;
    @Column(name="zipcode")
    private String zipcode;

    @OneToOne
    @JoinColumn(name = "geo_id", referencedColumnName = "id")
    private Geo geo;

    @JsonIgnore
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<User> users;

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                '}';
    }
}
