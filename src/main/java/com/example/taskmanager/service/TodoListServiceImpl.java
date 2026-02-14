package com.example.taskmanager.service;

import com.example.taskmanager.domain.TodoItem;
import com.example.taskmanager.domain.TodoList;
import com.example.taskmanager.repo.TodoItemRepository;
import com.example.taskmanager.repo.TodoListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TodoListServiceImpl implements TodoListService {
    private final TodoListRepository todoListRepository;
    private final TodoItemRepository todoItemRepository;

    public TodoListServiceImpl(TodoListRepository todoListRepository, TodoItemRepository todoItemRepository) {
        this.todoListRepository = todoListRepository;
        this.todoItemRepository = todoItemRepository;
    }

    @Override
    public List<TodoList> getAllLists() {
        return todoListRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public TodoList createList(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("List name cannot be empty");
        }
        TodoList list = new TodoList(name);
        return todoListRepository.save(list);
    }

    @Override
    public void deleteList(UUID listId) {
        todoListRepository.deleteById(listId);
    }

    @Override
    public void renameList(UUID listId, String newName) {
        TodoList list = todoListRepository.findById(listId)
            .orElseThrow(() -> new IllegalArgumentException("List not found"));
        list.setName(newName);
        todoListRepository.save(list);
    }

    @Override
    public TodoItem addItem(UUID listId, String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Item text cannot be empty");
        }
        TodoList list = todoListRepository.findById(listId)
            .orElseThrow(() -> new IllegalArgumentException("List not found"));
        TodoItem item = new TodoItem(text, list);
        return todoItemRepository.save(item);
    }

    @Override
    public void toggleItem(UUID itemId) {
        TodoItem item = todoItemRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setCompleted(!item.isCompleted());
        todoItemRepository.save(item);
    }

    @Override
    public void deleteItem(UUID itemId) {
        todoItemRepository.deleteById(itemId);
    }

    @Override
    public void updateItemText(UUID itemId, String newText) {
        TodoItem item = todoItemRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        item.setText(newText);
        todoItemRepository.save(item);
    }
}
