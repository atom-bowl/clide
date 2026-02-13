package com.example.taskmanager.dto;

import com.example.taskmanager.domain.TaskStatus;

public record TaskFilter(
        TaskStatus status,
        String search
) {
}
