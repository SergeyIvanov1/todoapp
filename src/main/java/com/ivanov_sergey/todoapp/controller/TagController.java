package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.dto.TagDTO;
import com.ivanov_sergey.todoapp.enums.TaskPriority;
import com.ivanov_sergey.todoapp.enums.TaskStatus;
import com.ivanov_sergey.todoapp.exception_handling.NoSuchTestEntityException;
import com.ivanov_sergey.todoapp.exception_handling.TaskIncorrectData;
import com.ivanov_sergey.todoapp.model.Tag;
import com.ivanov_sergey.todoapp.model.Task;
import com.ivanov_sergey.todoapp.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TagController {

    private final TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags(@RequestParam(required = false) String name) {
        try {
            List<Tag> tags = new ArrayList();

            if (name == null)
                tags.addAll(tagRepository.findAll());
            else
                tags.addAll(tagRepository.findByName(name));

            if (tags.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTaskById(@PathVariable("id") long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);

        if (tagOptional.isPresent()) {
            return new ResponseEntity<>(tagOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@RequestBody TagDTO tagDTO) {
        try {
            Tag savingTask = tagRepository.save(Tag.builder()
                    .name(tagDTO.getName())
                    .color(tagDTO.getColor())
                    .build());

            return new ResponseEntity<>(savingTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable("id") long id, @RequestBody TagDTO tagDTO) {
        Optional<Tag> tagData = tagRepository.findById(id);

        if (tagData.isPresent()) {
            Tag tagFromDB = tagData.get();
            tagFromDB.setName(tagDTO.getName());
            tagFromDB.setColor(tagDTO.getColor());

            return new ResponseEntity<>(tagRepository.save(tagFromDB), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") long id) {
        try {
            tagRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tags")
    public ResponseEntity<HttpStatus> deleteAllTags() {
        try {
            tagRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//    @GetMapping("/tasks/published")
//    public ResponseEntity<List<Task>> findByPublished() {
//        try {
//            List<Task> tutorials = taskRepository.findByPublished(true);
//
//            if (tutorials.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(tutorials, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @ExceptionHandler
    public ResponseEntity<TaskIncorrectData> handleException(NoSuchTestEntityException exception) {
        TaskIncorrectData data = new TaskIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<TaskIncorrectData> handleException(Exception exception) {
        TaskIncorrectData data = new TaskIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
