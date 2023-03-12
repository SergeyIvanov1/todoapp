package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.dto.TaskDTO;
import com.ivanov_sergey.todoapp.enums.TaskPriority;
import com.ivanov_sergey.todoapp.enums.TaskStatus;
import com.ivanov_sergey.todoapp.exception_handling.NoSuchEntityException;
import com.ivanov_sergey.todoapp.exception_handling.TaskIncorrectData;
import com.ivanov_sergey.todoapp.mapper.TaskMapper;
import com.ivanov_sergey.todoapp.persist.model.Tag;
import com.ivanov_sergey.todoapp.persist.model.Task;
import com.ivanov_sergey.todoapp.requests.TaskRequest;
import com.ivanov_sergey.todoapp.service.TagService;
import com.ivanov_sergey.todoapp.service.TaskService;
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
import java.util.Set;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/todo")
public class TaskController {

    private final TaskService taskService;
    private final TagService tagService;
    protected final TaskMapper taskMapper;

    @Autowired
    public TaskController(TaskService taskService, TagService tagService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.tagService = tagService;
        this.taskMapper = taskMapper;
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
    public ResponseEntity<List<TaskDTO>> getAllTasks(@RequestParam(required = false) String title) {
        List<Task> tasks = new ArrayList<>();
        try {
            if (title == null)
                taskService.findAllTasks().forEach(tasks::add);
            else
                taskService.findAllTasksByTitleContaining(title).forEach(tasks::add);

            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<TaskDTO> taskDTOS = taskMapper.tasksMapToDTOs(tasks);

        System.out.println("taskDTOS from getAllTasks = " + taskDTOS);
        return new ResponseEntity<>(taskDTOS, HttpStatus.CREATED);
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
        Optional<Task> taskOptional = taskService.findTaskById(id);
        Task taskFromDB;
        if (taskOptional.isPresent()) {
            taskFromDB = taskOptional.get();
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TaskDTO taskDTO = taskMapper.mapToDTO(taskFromDB);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @Operation(summary = "Creating a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskRequest.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest taskRequest) {
        Set<Tag> tagsFromTask = tagService.getTagsFromTask(taskRequest);
        System.out.println("taskRequest = " + taskRequest); // todo remove
        Task savingTask = Task.builder()
                    .title(taskRequest.getTitle())
                    .actualStartDate(taskRequest.getActualStartDate())
                    .actualEndDate(taskRequest.getActualEndDate())
                    .description(taskRequest.getDescription())
                    .content(taskRequest.getContent())
                    .status(TaskStatus.getTaskStatusByValue(taskRequest.getStatus()))
                    .priority(TaskPriority.getTaskPriorityByValue(taskRequest.getPriority()))
                    .hours(taskRequest.getHours())
                    .tags(tagsFromTask)
                    .build();
        System.out.println("savingTask = " + savingTask); // todo remove
        try {
            taskService.saveTaskToDB(savingTask);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("savingTask after save = " + savingTask); // todo remove
        return new ResponseEntity<>(savingTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Updating a task by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody TaskDTO taskDTO) {
        Optional<Task> taskData = taskService.findTaskById(id);

        if (taskData.isPresent()) {
            Task taskFromDB = taskData.get();
            taskFromDB.setTitle(taskDTO.getTitle());
            taskFromDB.setDescription(taskDTO.getDescription());
            taskFromDB.setContent(taskDTO.getContent());
            taskFromDB.setStatus(TaskStatus.getTaskStatusByValue(taskDTO.getStatus()));
            taskFromDB.setPriority(TaskPriority.getTaskPriorityByValue(taskDTO.getPriority()));
            return new ResponseEntity<>(taskService.saveTaskToDB(taskFromDB), HttpStatus.OK);
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
            taskService.deleteTaskById(id);
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
            taskService.deleteAllTasks();
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
    public ResponseEntity<TaskIncorrectData> handleException(NoSuchEntityException exception) {
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
