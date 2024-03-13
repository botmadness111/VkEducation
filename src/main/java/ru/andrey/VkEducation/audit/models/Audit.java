package ru.andrey.VkEducation.audit.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.andrey.VkEducation.security.models.UserVk;

import java.util.Date;

@Entity(name = "audit")
@Table
@Getter
@Setter
@NoArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "params")
    private String params;
    @Column(name="method")
    private String method;

    @ManyToOne
    @JoinColumn(name = "user_vk_id", referencedColumnName = "id")
    private UserVk userVk;
}
