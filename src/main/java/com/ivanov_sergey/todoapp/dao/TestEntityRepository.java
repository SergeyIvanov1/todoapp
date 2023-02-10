package com.ivanov_sergey.todoapp.dao;



import com.ivanov_sergey.todoapp.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
    public List<TestEntity> findAllByName(String name);
}
