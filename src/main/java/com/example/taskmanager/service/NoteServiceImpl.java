package com.example.taskmanager.service;

import com.example.taskmanager.domain.Note;
import com.example.taskmanager.repo.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Note createNote(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Note content cannot be empty");
        }
        Note note = new Note(content);
        return noteRepository.save(note);
    }

    @Override
    public void deleteNote(UUID noteId) {
        noteRepository.deleteById(noteId);
    }

    @Override
    public void updateNote(UUID noteId, String newContent) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new IllegalArgumentException("Note not found"));
        note.setContent(newContent);
        noteRepository.save(note);
    }
}
