package com.example.taskmanager.service;

import com.example.taskmanager.domain.TodoItem;
import com.example.taskmanager.domain.TodoList;

import java.util.List;
import java.util.UUID;

public interface TodoListService {
    List<TodoList> getAllLists();
    TodoList createList(String name);
    void deleteList(UUID listId);
    void renameList(UUID listId, String newName);

    TodoItem addItem(UUID listId, String text);
    void toggleItem(UUID itemId);
    void deleteItem(UUID itemId);
    void updateItemText(UUID itemId, String newText);
}
