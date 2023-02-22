package com.ivanov_sergey.todoapp.dto;

import com.ivanov_sergey.todoapp.enums.TaskPriority;
import com.ivanov_sergey.todoapp.enums.TaskStatus;
import com.ivanov_sergey.todoapp.model.Tag;
import com.ivanov_sergey.todoapp.model.TaskComment;
import com.ivanov_sergey.todoapp.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private String title;

    private String description;

    private String content;

    private String status;
//    private TaskStatus status;

    private String priority;
//    private TaskPriority priority;

    private Integer hours;

//    private User user;
//
//    private List<Tag> tags;
//
//    private List<TaskComment> comments;
}
