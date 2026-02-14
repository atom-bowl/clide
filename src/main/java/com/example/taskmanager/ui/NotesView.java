package com.example.taskmanager.ui;

import com.example.taskmanager.domain.Note;
import com.example.taskmanager.service.NoteService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Lazy
public class NotesView {
    private final NoteService noteService;
    private final BorderPane root = new BorderPane();
    private final VBox notesContainer = new VBox(10);
    private final TextArea inputArea = new TextArea();

    private static final DateTimeFormatter TIME_FORMATTER =
        DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm").withZone(ZoneId.systemDefault());

    public NotesView(NoteService noteService) {
        this.noteService = noteService;
        buildLayout();
        refreshNotes();
    }

    public Parent getRoot() {
        return root;
    }

    private void buildLayout() {
        root.setPadding(new Insets(18));

        Label header = new Label("Notes & Self-Chat");
        header.setFont(Font.font("System", FontWeight.BOLD, 18));

        ScrollPane notesScroll = new ScrollPane(notesContainer);
        notesScroll.setFitToWidth(true);
        notesScroll.setPrefHeight(400);
        notesScroll.setStyle("-fx-background-color: #f5f5f5;");
        notesContainer.setPadding(new Insets(10));
        notesContainer.setStyle("-fx-background-color: #f5f5f5;");

        VBox inputBox = new VBox(8);
        inputBox.setPadding(new Insets(10));

        Label inputLabel = new Label("Add a note:");
        inputLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        inputArea.setPromptText("Type your note here...");
        inputArea.setPrefRowCount(4);
        inputArea.setWrapText(true);

        HBox buttonBox = new HBox(8);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveButton = new Button("Save Note");
        Button clearButton = new Button("Clear");

        saveButton.setOnAction(e -> saveNote());
        clearButton.setOnAction(e -> inputArea.clear());

        buttonBox.getChildren().addAll(clearButton, saveButton);

        inputBox.getChildren().addAll(inputLabel, inputArea, buttonBox);

        VBox content = new VBox(15);
        content.getChildren().addAll(header, notesScroll, inputBox);
        VBox.setVgrow(notesScroll, Priority.ALWAYS);

        root.setCenter(content);
    }

    private void saveNote() {
        String content = inputArea.getText();
        if (content != null && !content.isBlank()) {
            try {
                noteService.createNote(content);
                inputArea.clear();
                refreshNotes();
            } catch (Exception ex) {
                showError("Error", ex.getMessage());
            }
        }
    }

    private void refreshNotes() {
        notesContainer.getChildren().clear();

        List<Note> notes = noteService.getAllNotes();

        if (notes.isEmpty()) {
            Label emptyLabel = new Label("No notes yet. Add one below!");
            emptyLabel.setStyle("-fx-text-fill: #888; -fx-font-style: italic;");
            notesContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Note note : notes) {
            VBox noteBox = new VBox(5);
            noteBox.setPadding(new Insets(10));
            noteBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

            HBox headerBox = new HBox(10);
            headerBox.setAlignment(Pos.CENTER_LEFT);

            Label timeLabel = new Label(TIME_FORMATTER.format(note.getCreatedAt()));
            timeLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
            timeLabel.setStyle("-fx-text-fill: #666;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-font-size: 10px;");
            deleteButton.setOnAction(e -> {
                noteService.deleteNote(note.getId());
                refreshNotes();
            });

            headerBox.getChildren().addAll(timeLabel, spacer, deleteButton);

            Label contentLabel = new Label(note.getContent());
            contentLabel.setWrapText(true);
            contentLabel.setFont(Font.font("System", 13));

            noteBox.getChildren().addAll(headerBox, contentLabel);
            notesContainer.getChildren().add(noteBox);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refresh() {
        refreshNotes();
    }
}
