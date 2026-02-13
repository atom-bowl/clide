package com.example.taskmanager.dto;

import com.example.taskmanager.domain.TaskStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
