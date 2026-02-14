package com.example.taskmanager.service;

import com.example.taskmanager.domain.Note;

import java.util.List;
import java.util.UUID;

public interface NoteService {
    List<Note> getAllNotes();
    Note createNote(String content);
    void deleteNote(UUID noteId);
    void updateNote(UUID noteId, String newContent);
}
