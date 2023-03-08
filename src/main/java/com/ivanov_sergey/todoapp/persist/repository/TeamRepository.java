package com.ivanov_sergey.todoapp.persist.repository;

import com.ivanov_sergey.todoapp.persist.model.Profile;
import com.ivanov_sergey.todoapp.persist.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByName(String name);
}
