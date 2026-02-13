package com.example.taskmanager.ui;

import com.example.taskmanager.domain.TaskStatus;
import com.example.taskmanager.dto.CreateTaskCommand;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.TaskFilter;
import com.example.taskmanager.dto.UpdateTaskCommand;
import com.example.taskmanager.service.TaskService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    private final TaskService taskService;

    public MainController(TaskService taskService) {
        this.taskService = taskService;
    }

    public List<TaskDto> loadTasks(TaskStatus status, String search) {
        return taskService.list(new TaskFilter(status, search));
    }

    public TaskDto save(UUID id, String title, String description, LocalDate dueDate) {
        if (id == null) {
            return taskService.create(new CreateTaskCommand(title, description, dueDate));
        }
        return taskService.update(id, new UpdateTaskCommand(title, description, dueDate));
    }

    public void delete(UUID id) {
        taskService.delete(id);
    }

    public TaskDto toggle(UUID id, boolean completed) {
        return taskService.toggleComplete(id, completed);
    }
}
