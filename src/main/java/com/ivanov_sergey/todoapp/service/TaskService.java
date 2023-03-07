package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.persist.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAllTasks();
    List<Task> findAllTasksByTitleContaining(String title);
    Optional<Task> findTaskById(long id);
    Task saveTaskToDB(Task task);
    void deleteTaskById(long id);
    void deleteAllTasks();
}
