package com.ivanov_sergey.todoapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static java.util.Objects.isNull;

@Getter
@AllArgsConstructor
public enum TaskPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH ("High");

    private final String value;

    public static TaskPriority getTaskPriorityByValue(String value){
        if(isNull(value) || value.isEmpty()){
            return null;
        }

        TaskPriority[] taskPriorities = TaskPriority.values();
        for (TaskPriority taskPriority : taskPriorities) {
            if (taskPriority.value.equals(value)){
                return taskPriority;
            }
        }
        return null;
    }

    public static String getValueByTaskPriority(TaskPriority taskPriority){
        return taskPriority.getValue();
    }

    public static List<TaskPriority> getAll(){
        return List.of(values());
    }
}
