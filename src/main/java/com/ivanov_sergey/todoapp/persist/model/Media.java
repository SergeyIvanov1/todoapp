package com.ivanov_sergey.todoapp.persist.model;

import com.ivanov_sergey.todoapp.enums.FriendRequestStatus;
import com.ivanov_sergey.todoapp.enums.MediaType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "media")
public class Media {
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

    @Column(name = "media_type")
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(name = "name")
    private String name;

    @Column(name = "filename")
    private String filename;

    @CreationTimestamp
    @Column(name = "create_at")
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updateAt;
}
