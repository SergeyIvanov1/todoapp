package com.ivanov_sergey.todoapp.persist.model;

import com.ivanov_sergey.todoapp.enums.MediaType;
import com.ivanov_sergey.todoapp.enums.ReactionType;
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
@Table(name = "reactions")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @OneToOne(mappedBy = "friend_requests", cascade = CascadeType.ALL)
    @OneToOne(targetEntity = User.class,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(targetEntity = User.class,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH})
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(name = "reaction_type")
    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    @CreationTimestamp
    @Column(name = "create_at")
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updateAt;
}
