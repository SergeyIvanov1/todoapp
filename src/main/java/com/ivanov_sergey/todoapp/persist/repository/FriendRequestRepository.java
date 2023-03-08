package com.ivanov_sergey.todoapp.persist.repository;

import com.ivanov_sergey.todoapp.persist.model.FriendRequest;
import com.ivanov_sergey.todoapp.persist.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
//    List<FriendRequest> findByName(String name);
}
