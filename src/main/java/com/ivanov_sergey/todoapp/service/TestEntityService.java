package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.entity.TestEntity;

import java.util.List;

public interface TestEntityService {

    public List<TestEntity> getAllTestEntities();

    public void saveTestEntity(TestEntity testEntity);
    public TestEntity getTestEntity(Long id);

    public void deleteTestEntity(Long id);
    public List<TestEntity> findAllByName(String name);
}
