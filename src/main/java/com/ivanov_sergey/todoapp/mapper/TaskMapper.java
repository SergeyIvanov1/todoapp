package com.ivanov_sergey.todoapp.mapper;

import com.ivanov_sergey.todoapp.dto.TaskDTO;
import com.ivanov_sergey.todoapp.persist.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    List<TaskDTO> tasksMapToDTOs (List<Task> tasks);
    @Mapping(target = "actualStartDate", source = "actualStartDate", dateFormat = "dd-MM-yyyy HH:mm")
    @Mapping(target = "actualEndDate", source = "actualEndDate", dateFormat = "dd-MM-yyyy HH:mm")
    TaskDTO mapToDTO (Task task);
}
