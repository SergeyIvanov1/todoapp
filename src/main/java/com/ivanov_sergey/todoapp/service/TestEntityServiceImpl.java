package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.dao.TestEntityRepository;
import com.ivanov_sergey.todoapp.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestEntityServiceImpl implements TestEntityService {

    @Autowired
    private TestEntityRepository testEntityRepository;

    @Override
    public List<TestEntity> getAllTestEntities() {
        return testEntityRepository.findAll();
    }

    @Override
    public TestEntity getTestEntity(Long id) {
        TestEntity testEntity = null;
        Optional<TestEntity> testEntityOptional = testEntityRepository.findById(id);
        if (testEntityOptional.isPresent()){
            testEntity = testEntityOptional.get();
        }
        return testEntity;
    }

    @Override
    public void saveTestEntity(TestEntity testEntity) {
        testEntityRepository.save(testEntity);
    }

    @Override
    public void deleteTestEntity(Long id) {
        testEntityRepository.deleteById(id);
    }

    @Override
    public List<TestEntity> findAllByName(String name) {
        return testEntityRepository.findAllByName(name);
    }
}
