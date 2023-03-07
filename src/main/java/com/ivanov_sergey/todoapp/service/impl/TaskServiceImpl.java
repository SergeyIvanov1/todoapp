package com.ivanov_sergey.todoapp.service.impl;

import com.ivanov_sergey.todoapp.mapper.TaskMapper;
import com.ivanov_sergey.todoapp.persist.model.Task;
import com.ivanov_sergey.todoapp.persist.repository.TaskRepository;
import com.ivanov_sergey.todoapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }




    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findAllTasksByTitleContaining(String title) {
        return taskRepository.findByTitleContaining(title);
    }

    public Optional<Task> findTaskById(long id) {
        return taskRepository.findById(id);
    }

    public Task saveTaskToDB(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTaskById(long id) {
        taskRepository.deleteById(id);
    }
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

}
