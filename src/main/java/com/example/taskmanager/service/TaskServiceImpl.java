package com.example.taskmanager.service;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.TaskStatus;
import com.example.taskmanager.dto.CreateTaskCommand;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.TaskFilter;
import com.example.taskmanager.dto.UpdateTaskCommand;
import com.example.taskmanager.repo.TaskRepository;
import com.example.taskmanager.search.TaskSearch;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final Validator validator;

    public TaskServiceImpl(TaskRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public TaskDto create(@Valid CreateTaskCommand command) {
        validate(command);

        Task task = new Task();
        task.setTitle(command.title().trim());
        task.setDescription(normalizeDescription(command.description()));
        task.setDueDate(command.dueDate());
        task.setStatus(TaskStatus.ACTIVE);

        return toDto(repository.save(task));
    }

    @Override
    public TaskDto update(UUID id, @Valid UpdateTaskCommand command) {
        validate(command);

        Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(command.title().trim());
        task.setDescription(normalizeDescription(command.description()));
        task.setDueDate(command.dueDate());

        return toDto(repository.save(task));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public TaskDto toggleComplete(UUID id, boolean completed) {
        Task task = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(completed ? TaskStatus.COMPLETED : TaskStatus.ACTIVE);
        return toDto(repository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> list(TaskFilter filter) {
        TaskFilter effective = filter == null ? new TaskFilter(null, null) : filter;
        List<Task> tasks;

        if (effective.status() != null) {
            tasks = repository.findByStatusOrderByDueDateAscCreatedAtDesc(effective.status());
        } else {
            tasks = repository.findAllByOrderByDueDateAscCreatedAtDesc();
        }

        tasks = TaskSearch.filterAndRank(tasks, effective.search());
        return tasks.stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDto> get(UUID id) {
        return repository.findById(id).map(this::toDto);
    }

    private void validate(Object command) {
        var violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }
        String trimmed = description.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
