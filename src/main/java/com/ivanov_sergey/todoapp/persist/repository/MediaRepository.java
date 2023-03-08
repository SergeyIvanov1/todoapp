package com.ivanov_sergey.todoapp.persist.repository;

import com.ivanov_sergey.todoapp.persist.model.FriendRequest;
import com.ivanov_sergey.todoapp.persist.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByName(String name);
}
