package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.dto.TaskDTO;
import com.ivanov_sergey.todoapp.model.Tag;

import java.util.Set;

public interface TagService {
    Set<Tag> getTagsFromTask(TaskDTO taskDTO);
}
