package com.example.taskmanager.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanager.domain.TaskStatus;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.TaskService;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {
    @Mock
    private TaskService taskService;

    @Test
    void saveShouldCreateWhenIdIsNull() {
        MainController controller = new MainController(taskService);
        UUID id = UUID.randomUUID();
        when(taskService.create(any())).thenReturn(new TaskDto(id, "A", null, null, TaskStatus.ACTIVE, Instant.now(), Instant.now()));

        TaskDto result = controller.save(null, "A", "", null);

        assertEquals(id, result.id());
        verify(taskService).create(any());
    }

    @Test
    void saveShouldUpdateWhenIdExists() {
        MainController controller = new MainController(taskService);
        UUID id = UUID.randomUUID();
        when(taskService.update(any(), any())).thenReturn(new TaskDto(id, "Updated", null, null, TaskStatus.ACTIVE, Instant.now(), Instant.now()));

        TaskDto result = controller.save(id, "Updated", "", LocalDate.now());

        assertEquals("Updated", result.title());
        verify(taskService).update(any(), any());
    }

    @Test
    void loadTasksShouldPassFilter() {
        MainController controller = new MainController(taskService);
        when(taskService.list(any())).thenReturn(List.of());

        controller.loadTasks(TaskStatus.ACTIVE, "term");

        verify(taskService).list(any());
    }

    @Test
    void deleteAndToggleShouldDelegate() {
        MainController controller = new MainController(taskService);
        UUID id = UUID.randomUUID();

        controller.delete(id);
        controller.toggle(id, true);

        verify(taskService).delete(id);
        verify(taskService).toggleComplete(id, true);
    }
}
