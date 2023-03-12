package com.ivanov_sergey.todoapp.dto;

import com.ivanov_sergey.todoapp.persist.model.Task;
import com.ivanov_sergey.todoapp.persist.model.TaskComment;
import com.ivanov_sergey.todoapp.persist.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentDTO {

    private Timestamp updateAt;
    private String text;
    private Long taskId;
    private String username;
    private List<TaskComment> comments;
}
