package com.ivanov_sergey.todoapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@Getter
@AllArgsConstructor
public enum TaskStatus {
    TODO("To Do"),
    IN_PROGRESS("In progress"),
    CODE_REVIEW ("Code review"),
    TESTS("Tests"),
    DONE("Done");

    private final String value;

    public static TaskStatus getTaskStatusByValue(String value){
        if(isNull(value) || value.isEmpty()){
            return null;
        }

        TaskStatus[] taskStatuses = TaskStatus.values();
        for (TaskStatus taskStatus : taskStatuses) {
            if (taskStatus.value.equals(value)){
                return taskStatus;
            }
        }
        return null;
    }

    public static List<TaskStatus> getAll(){
        return List.of(values());
    }
}
