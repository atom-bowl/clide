package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTaskCommand;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.TaskFilter;
import com.example.taskmanager.dto.UpdateTaskCommand;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    TaskDto create(CreateTaskCommand command);

    TaskDto update(UUID id, UpdateTaskCommand command);

    void delete(UUID id);

    TaskDto toggleComplete(UUID id, boolean completed);

    List<TaskDto> list(TaskFilter filter);

    Optional<TaskDto> get(UUID id);
}
