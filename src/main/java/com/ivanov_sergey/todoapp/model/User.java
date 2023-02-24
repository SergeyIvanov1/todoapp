package com.ivanov_sergey.todoapp.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public User() {
        super();
        this.enabled=false;
    }
//    @CreationTimestamp
//    @Column(name = "create_at")
//    private Timestamp createAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private Timestamp updateAt;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

//    @Column(name = "username")
//    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @CreationTimestamp
    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "locked")
    private boolean locked;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
