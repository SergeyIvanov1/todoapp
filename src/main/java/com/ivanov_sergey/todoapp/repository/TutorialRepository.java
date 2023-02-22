package com.ivanov_sergey.todoapp.repository;

import com.ivanov_sergey.todoapp.model.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByPublished(boolean published);

    List<Tutorial> findByTitleContaining(String title);
}
