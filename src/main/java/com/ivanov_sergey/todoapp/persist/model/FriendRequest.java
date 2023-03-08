package com.ivanov_sergey.todoapp.persist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivanov_sergey.todoapp.enums.FriendRequestStatus;
import com.ivanov_sergey.todoapp.enums.TaskPriority;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
//@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @OneToOne(mappedBy = "friend_requests", cascade = CascadeType.ALL)
    @OneToOne(targetEntity = User.class,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH})
    @JoinColumn(name = "initiator_user_id")
    private User initiatorUser;

    @OneToOne(targetEntity = User.class,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH})
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    @CreationTimestamp
    @Column(name = "create_at")
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updateAt;
}
