package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.dto.TaskDTO;
import com.ivanov_sergey.todoapp.persist.model.Tag;
import com.ivanov_sergey.todoapp.requests.TaskRequest;

import java.util.Set;

public interface TagService {
    Set<Tag> getTagsFromTask(TaskRequest taskRequest);
}
