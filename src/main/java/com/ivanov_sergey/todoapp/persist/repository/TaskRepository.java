package com.ivanov_sergey.todoapp.persist.repository;

import com.ivanov_sergey.todoapp.persist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTitleContaining(String title);
}
