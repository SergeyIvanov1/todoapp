package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.dto.TaskCommentDTO;
import com.ivanov_sergey.todoapp.exception_handling.NoSuchEntityException;
import com.ivanov_sergey.todoapp.persist.model.Task;
import com.ivanov_sergey.todoapp.persist.model.TaskComment;
import com.ivanov_sergey.todoapp.persist.model.User;
import com.ivanov_sergey.todoapp.persist.repository.TaskCommentRepository;
import com.ivanov_sergey.todoapp.persist.repository.TaskRepository;
import com.ivanov_sergey.todoapp.persist.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TaskCommentController {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskCommentController(TaskCommentRepository taskCommentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get a taskComment by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/taskComments/{id}")
    public ResponseEntity<TaskComment> getTaskCommentById(@PathVariable("id") long id) {
        Optional<TaskComment> taskCommentOptional = taskCommentRepository.findById(id);

        if (taskCommentOptional.isPresent()) {
            return new ResponseEntity<>(taskCommentOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Creating a taskCommentDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskCommentDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/taskComments")
    public ResponseEntity<TaskCommentDTO> createTaskComment(@RequestBody TaskCommentDTO taskCommentDTO) {
        System.out.println("taskCommentDTO = " + taskCommentDTO); //todo remove
        try {
            Optional<Task> optionalTask = taskRepository.findById(taskCommentDTO.getTaskId());
            Task task;
            if (optionalTask.isPresent()) {
                task = optionalTask.get();
            } else {
                throw new NoSuchEntityException("Task is not found");
            }

            System.out.println("task = " + task); //todo remove

            Optional<User> optionalUser = userRepository.findByUsername(taskCommentDTO.getUsername());
            User user;
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
            } else {
                throw new NoSuchEntityException("Task is not found");
            }

            System.out.println("user = " + user); //todo remove

            TaskComment savingTask = taskCommentRepository.save(TaskComment.builder()
                    .text(taskCommentDTO.getText())
                    .task(task)
                    .user(user)
                    .build());

            System.out.println("savingTask = " + savingTask); //todo remove

            TaskCommentDTO response = TaskCommentDTO.builder()
                    .updateAt(savingTask.getUpdateAt())
                    .username(savingTask.getUser().getUsername())
                    .comments(savingTask.getTask().getComments())
                    .build();

            System.out.println("response = " + response); //todo remove

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Updating a taskComment by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskComment.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/taskComments/{id}")
    public ResponseEntity<TaskComment> updateTaskComment(@PathVariable("id") long id, @RequestBody TaskComment taskComment) {
        Optional<TaskComment> taskCommentData = taskCommentRepository.findById(id);

        if (taskCommentData.isPresent()) {
            TaskComment taskCommentFromDB = taskCommentData.get();
//            taskCommentFromDB.setName(taskComment.getName());
//            taskCommentFromDB.setColor(taskComment.getColor());

            return new ResponseEntity<>(taskCommentRepository.save(taskCommentFromDB), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deleting a taskComment by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/taskComments/{id}")
    public ResponseEntity<HttpStatus> deleteTaskComment(@PathVariable("id") long id) {
        try {
            taskCommentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
