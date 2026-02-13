package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateTaskCommand(
        @NotBlank @Size(max = 120) String title,
        @Size(max = 2000) String description,
        LocalDate dueDate
) {
}
