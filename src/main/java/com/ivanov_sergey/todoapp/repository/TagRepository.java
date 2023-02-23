package com.ivanov_sergey.todoapp.repository;

import com.ivanov_sergey.todoapp.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByName(String name);
}
