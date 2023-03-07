package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.dto.TagDTO;
import com.ivanov_sergey.todoapp.enums.TaskPriority;
import com.ivanov_sergey.todoapp.enums.TaskStatus;
import com.ivanov_sergey.todoapp.exception_handling.NoSuchTestEntityException;
import com.ivanov_sergey.todoapp.exception_handling.TaskIncorrectData;
import com.ivanov_sergey.todoapp.model.Tag;
import com.ivanov_sergey.todoapp.model.Task;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.repository.TagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TagController {

    private final TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Operation(summary = "Getting all tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/tags")
    public ResponseEntity<Set<Tag>> getAllTags(@RequestParam(required = false) String name) {
        Set<Tag> tags = new HashSet<>();

        try {

            if (name == null)
                tags.addAll(tagRepository.findAll());
            else
                tags.addAll(tagRepository.findByName(name));

            if (tags.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tags, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a tag by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTaskById(@PathVariable("id") long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);

        if (tagOptional.isPresent()) {
            return new ResponseEntity<>(tagOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Creating a tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
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

    @Operation(summary = "Updating a tag by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
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

    @Operation(summary = "Deleting a tag by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") long id) {
        try {
            tagRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deleting all tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/tags")
    public ResponseEntity<HttpStatus> deleteAllTags() {
        try {
            tagRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

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
