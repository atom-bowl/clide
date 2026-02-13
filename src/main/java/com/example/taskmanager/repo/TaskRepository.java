package com.example.taskmanager.repo;

import com.example.taskmanager.domain.Task;
import com.example.taskmanager.domain.TaskStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByStatusOrderByDueDateAscCreatedAtDesc(TaskStatus status);

    List<Task> findAllByOrderByDueDateAscCreatedAtDesc();
}
