package com.ivanov_sergey.todoapp.requests;

import com.ivanov_sergey.todoapp.persist.model.Tag;
import com.ivanov_sergey.todoapp.persist.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String status;
    private String priority;
    private Integer hours;

    private Timestamp actualStartDate;
    private Timestamp actualEndDate;
    private User user;
    private Set<Tag> tags = new HashSet<>();
//    private List<TaskComment> comments;
}
