package com.example.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.TaskStatus;
import com.example.taskmanager.dto.CreateTaskCommand;
import com.example.taskmanager.dto.TaskFilter;
import com.example.taskmanager.dto.UpdateTaskCommand;
import com.example.taskmanager.repo.TaskRepository;
import jakarta.validation.Validation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository repository;

    private TaskServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TaskServiceImpl(
                repository,
                Validation.buildDefaultValidatorFactory().getValidator()
        );
    }

    @Test
    void createShouldTrimAndPersistFields() {
        when(repository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(UUID.randomUUID());
            return task;
        });

        var dto = service.create(new CreateTaskCommand("  Draft release notes  ", "  desc  ", LocalDate.now()));

        assertEquals("Draft release notes", dto.title());
        assertEquals("desc", dto.description());
        assertEquals(TaskStatus.ACTIVE, dto.status());
        verify(repository).save(any(Task.class));
    }

    @Test
    void updateShouldThrowIfTaskMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () ->
                service.update(id, new UpdateTaskCommand("title", "", null))
        );
    }

    @Test
    void toggleCompleteShouldSetCompletedStatus() {
        UUID id = UUID.randomUUID();
        Task task = new Task();
        task.setId(id);
        task.setTitle("T1");
        task.setStatus(TaskStatus.ACTIVE);

        when(repository.findById(id)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(task);

        var dto = service.toggleComplete(id, true);

        assertEquals(TaskStatus.COMPLETED, dto.status());
    }

    @Test
    void listShouldUseStatusAndSearchBranch() {
        when(repository.findByStatusOrderByDueDateAscCreatedAtDesc(TaskStatus.ACTIVE)).thenReturn(List.of());

        service.list(new TaskFilter(TaskStatus.ACTIVE, "build"));

        verify(repository).findByStatusOrderByDueDateAscCreatedAtDesc(TaskStatus.ACTIVE);
    }
}
