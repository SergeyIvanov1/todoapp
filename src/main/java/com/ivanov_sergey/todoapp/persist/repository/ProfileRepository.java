package com.ivanov_sergey.todoapp.persist.repository;

import com.ivanov_sergey.todoapp.persist.model.FriendRequest;
import com.ivanov_sergey.todoapp.persist.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
//    List<Profile> findByName(String name);
}
