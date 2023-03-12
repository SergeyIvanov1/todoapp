package com.ivanov_sergey.todoapp.dto;

import com.ivanov_sergey.todoapp.persist.model.Tag;
import com.ivanov_sergey.todoapp.persist.model.TaskComment;
import com.ivanov_sergey.todoapp.persist.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private String content;
    private String status;
    private String priority;
    private Integer hours;

    private String actualStartDate;
    private String actualEndDate;
    private User user;
    private Set<Tag> tags = new HashSet<>();
    private List<TaskComment> comments;
}
