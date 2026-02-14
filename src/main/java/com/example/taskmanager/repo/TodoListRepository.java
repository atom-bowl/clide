package com.example.taskmanager.repo;

import com.example.taskmanager.domain.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, UUID> {
    List<TodoList> findAllByOrderByUpdatedAtDesc();
}
