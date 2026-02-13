package com.example.taskmanager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.taskmanager.domain.TaskStatus;
import com.example.taskmanager.dto.CreateTaskCommand;
import com.example.taskmanager.dto.TaskFilter;
import com.example.taskmanager.dto.UpdateTaskCommand;
import com.example.taskmanager.service.TaskService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceIntegrationTest {
    @Autowired
    private TaskService taskService;

    @Test
    void shouldCreateUpdateToggleAndFilterTasks() {
        var created = taskService.create(new CreateTaskCommand("Write docs", "Draft API docs", LocalDate.now().plusDays(1)));
        assertEquals(TaskStatus.ACTIVE, created.status());

        var updated = taskService.update(
                created.id(),
                new UpdateTaskCommand("Write docs v2", "Finalize", LocalDate.now().plusDays(2))
        );
        assertEquals("Write docs v2", updated.title());

        var completed = taskService.toggleComplete(updated.id(), true);
        assertEquals(TaskStatus.COMPLETED, completed.status());

        var completedList = taskService.list(new TaskFilter(TaskStatus.COMPLETED, null));
        assertTrue(completedList.stream().anyMatch(task -> task.id().equals(updated.id())));
    }
}
