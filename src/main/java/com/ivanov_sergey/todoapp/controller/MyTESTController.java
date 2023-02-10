package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.entity.TestEntity;
import com.ivanov_sergey.todoapp.exception_handling.NoSuchTestEntityException;
import com.ivanov_sergey.todoapp.exception_handling.TestEntityIncorrectData;
import com.ivanov_sergey.todoapp.service.TestEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyTESTController {

    private final TestEntityService testEntityService;

    @Autowired
    public MyTESTController(TestEntityService testEntityService) {
        this.testEntityService = testEntityService;
    }

    @GetMapping( "/entities")
    public List<TestEntity> showAllTestEntities(){
        return testEntityService.getAllTestEntities();
    }

    @GetMapping("/entities/{id}") // this {id} passing to the parameter of method
    public TestEntity getTestEntity(@PathVariable Long id){ // @PathVariable is using for getting value of request from address
        TestEntity testEntity = testEntityService.getTestEntity(id);

        if (testEntity == null){
            throw new NoSuchTestEntityException("There is no testEntity with ID = " + id + " in the DataBase");
        }
        return testEntity; //passing is not object testEntity and JSON, because Spring and Jackson
    }

    @PostMapping("/entities")
    public TestEntity addNewTestEntity(@RequestBody TestEntity testEntity){ //we are passing JSON? but Spring convert parameter to class
        testEntityService.saveTestEntity(testEntity);
        return testEntity;
    }

    @PutMapping("/entities")
    public TestEntity updateTestEntity(@RequestBody TestEntity testEntity){
        testEntityService.saveTestEntity(testEntity);
        return testEntity;
    }

    @DeleteMapping("/entities/{id}")
    public String deletetestEntity(@PathVariable Long id){
        TestEntity testEntity = testEntityService.getTestEntity(id);
        if (testEntity == null){
            throw new NoSuchTestEntityException("There is no testEntity with ID = " + id + " in the DataBase");
        }


        testEntityService.deleteTestEntity(id);
        return "TestEntity with id = " + id + " was deleted";
    }

    @GetMapping("/entities/name/{name}")
    public List<TestEntity> showAllTestEntitiesByName(@PathVariable String name){
        return  testEntityService.findAllByName(name);
    }

    @ExceptionHandler
    public ResponseEntity<TestEntityIncorrectData> handleException(NoSuchTestEntityException exception){
        TestEntityIncorrectData data = new TestEntityIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<TestEntityIncorrectData> handleException(Exception exception){
        TestEntityIncorrectData data = new TestEntityIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
