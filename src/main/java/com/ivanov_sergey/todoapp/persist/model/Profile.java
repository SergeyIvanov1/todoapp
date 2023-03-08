package com.ivanov_sergey.todoapp.persist.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "gender")
    private char gender;

    @Column(name = "birthday")
    private Date birthday;

    @OneToOne(targetEntity = User.class,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH})
    @JoinColumn(name = "photo_id")
    private Media avatar;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "hometown")
    private String hometown;

    @CreationTimestamp
    @Column(name = "create_at")
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updateAt;
}
