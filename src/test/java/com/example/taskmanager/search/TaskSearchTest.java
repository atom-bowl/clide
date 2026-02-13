package com.example.taskmanager.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.TaskStatus;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TaskSearchTest {
    @Test
    void shouldFilterOutNonMatchingTasksAndRankTitleMatchesFirst() {
        Task strong = new Task();
        strong.setId(UUID.randomUUID());
        strong.setTitle("Build release pipeline");
        strong.setDescription("pipeline checks");
        strong.setStatus(TaskStatus.ACTIVE);

        Task weak = new Task();
        weak.setId(UUID.randomUUID());
        weak.setTitle("Prepare docs");
        weak.setDescription("Need build steps");
        weak.setStatus(TaskStatus.ACTIVE);

        Task missing = new Task();
        missing.setId(UUID.randomUUID());
        missing.setTitle("Plan meeting");
        missing.setDescription("agenda");
        missing.setStatus(TaskStatus.ACTIVE);

        List<Task> ranked = TaskSearch.filterAndRank(List.of(weak, missing, strong), "build");

        assertEquals(2, ranked.size());
        assertEquals(strong.getId(), ranked.get(0).getId());
    }
}
