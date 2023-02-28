package com.ivanov_sergey.todoapp.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

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

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "role_id", referencedColumnName = "id")
//    private Role role;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST,
                       CascadeType.MERGE,
                       CascadeType.REFRESH,
                       CascadeType.DETACH})
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private VerificationToken verificationToken;

    public User() {
        super();
        this.enabled = false;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return emailVerified == user.emailVerified && enabled == user.enabled && locked == user.locked && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(password, user.password) && Objects.equals(country, user.country) && Objects.equals(registeredAt, user.registeredAt) && Objects.equals(email, user.email) && Objects.equals(roles, user.roles) && Objects.equals(verificationToken, user.verificationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, password, country, registeredAt, email, emailVerified, enabled, locked, roles, verificationToken);
    }
}
