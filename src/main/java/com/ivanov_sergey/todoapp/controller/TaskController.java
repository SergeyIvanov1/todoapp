package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.dto.TaskDTO;
import com.ivanov_sergey.todoapp.enums.TaskPriority;
import com.ivanov_sergey.todoapp.enums.TaskStatus;
import com.ivanov_sergey.todoapp.exception_handling.NoSuchTestEntityException;
import com.ivanov_sergey.todoapp.exception_handling.TaskIncorrectData;
import com.ivanov_sergey.todoapp.model.Task;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/todo")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Operation(summary = "Getting all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content),
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) String title) {
        try {
            List<Task> tasks = new ArrayList<>();

            if (title == null)
                taskRepository.findAll().forEach(tasks::add);
            else
                taskRepository.findByTitleContaining(title).forEach(tasks::add);

            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(tasks, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get a task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        Task taskFromDB;
        if (taskOptional.isPresent()) {
            taskFromDB = taskOptional.get();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TaskDTO taskDTO = TaskDTO.builder()
                .id(taskFromDB.getId())
                .title(taskFromDB.getTitle())
                .description(taskFromDB.getDescription())
                .content(taskFromDB.getContent())
                .status(TaskStatus.getValueByTaskStatus(taskFromDB.getStatus()))
                .priority(TaskPriority.getValueByTaskPriority(taskFromDB.getPriority()))
                .hours(taskFromDB.getHours())
                .build();
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);

//        if (taskOptional.isPresent()) {
//            return new ResponseEntity<>(taskOptional.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
    }

    @Operation(summary = "Creating a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
        try {
            Task savingTask = taskRepository.save(Task.builder()
                    .title(taskDTO.getTitle())
                    .description(taskDTO.getDescription())
                    .content(taskDTO.getContent())
                    .status(TaskStatus.getTaskStatusByValue(taskDTO.getStatus()))
                    .priority(TaskPriority.getTaskPriorityByValue(taskDTO.getPriority()))
                    .hours(Integer.valueOf(taskDTO.getHours()))
                    .build());

            return new ResponseEntity<>(savingTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Updating a task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody TaskDTO taskDTO) {
        Optional<Task> taskData = taskRepository.findById(id);

        if (taskData.isPresent()) {
            Task taskFromDB = taskData.get();
            taskFromDB.setTitle(taskDTO.getTitle());
            taskFromDB.setDescription(taskDTO.getDescription());
            taskFromDB.setContent(taskDTO.getContent());
            taskFromDB.setStatus(TaskStatus.getTaskStatusByValue(taskDTO.getStatus()));
            taskFromDB.setPriority(TaskPriority.getTaskPriorityByValue(taskDTO.getPriority()));
            return new ResponseEntity<>(taskRepository.save(taskFromDB), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deleting a task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") long id) {
        try {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deleting all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/tasks")
    public ResponseEntity<HttpStatus> deleteAllTasks() {
        try {
            taskRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
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
