package com.example.taskmanager.ui;

import com.example.taskmanager.domain.TaskStatus;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.service.TaskNotFoundException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Lazy
public class TasksView {
    private static final String FILTER_ALL = "All";
    private static final String FILTER_ACTIVE = "Active";
    private static final String FILTER_COMPLETED = "Completed";

    private final MainController controller;
    private final BorderPane root = new BorderPane();
    private final ObservableList<TaskDto> tableItems = FXCollections.observableArrayList();

    private final TableView<TaskDto> taskTable = new TableView<>(tableItems);
    private final TextField titleField = new TextField();
    private final TextArea descriptionField = new TextArea();
    private final DatePicker dueDatePicker = new DatePicker();
    private final ComboBox<String> filterBox = new ComboBox<>();
    private final TextField searchField = new TextField();

    private UUID selectedTaskId;

    public TasksView(MainController controller) {
        this.controller = controller;
        buildLayout();
        wireEvents();
        refreshTable();
    }

    public Parent getRoot() {
        return root;
    }

    private void buildLayout() {
        root.setPadding(new Insets(18));

        HBox filters = new HBox(10);
        filterBox.setItems(FXCollections.observableArrayList(FILTER_ALL, FILTER_ACTIVE, FILTER_COMPLETED));
        filterBox.setValue(FILTER_ALL);
        searchField.setPromptText("Search title or description");
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshTable());

        filters.getChildren().addAll(new Label("Status:"), filterBox, searchField, refreshButton);
        filters.setAlignment(Pos.CENTER_LEFT);

        setupTable();

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.setPadding(new Insets(0, 0, 0, 16));

        titleField.setPromptText("Title");
        descriptionField.setPromptText("Description");
        descriptionField.setPrefRowCount(6);

        form.add(new Label("Title"), 0, 0);
        form.add(titleField, 0, 1);
        form.add(new Label("Due Date"), 0, 2);
        form.add(dueDatePicker, 0, 3);
        form.add(new Label("Description"), 0, 4);
        form.add(descriptionField, 0, 5);

        HBox buttons = new HBox(8);
        Button newButton = new Button("New");
        Button saveButton = new Button("Save");
        Button deleteButton = new Button("Delete");
        Button toggleButton = new Button("Toggle Complete");

        newButton.setOnAction(event -> clearForm());
        saveButton.setOnAction(event -> saveTask());
        deleteButton.setOnAction(event -> deleteTask());
        toggleButton.setOnAction(event -> toggleTask());

        buttons.getChildren().addAll(newButton, saveButton, deleteButton, toggleButton);
        form.add(buttons, 0, 6);

        HBox content = new HBox(12, taskTable, form);
        HBox.setHgrow(taskTable, Priority.ALWAYS);
        taskTable.setMinWidth(560);

        root.setTop(filters);
        root.setCenter(content);
    }

    private void setupTable() {
        TableColumn<TaskDto, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().title()));
        titleCol.setPrefWidth(260);

        TableColumn<TaskDto, TaskStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().status()));
        statusCol.setPrefWidth(120);

        TableColumn<TaskDto, java.time.LocalDate> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().dueDate()));
        dueCol.setPrefWidth(130);

        TableColumn<TaskDto, java.time.Instant> updatedCol = new TableColumn<>("Updated");
        updatedCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().updatedAt()));
        updatedCol.setPrefWidth(220);

        taskTable.getColumns().addAll(titleCol, statusCol, dueCol, updatedCol);
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        taskTable.setRowFactory(table -> {
            TableRow<TaskDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    populateForm(row.getItem());
                }
            });
            return row;
        });
    }

    private void wireEvents() {
        filterBox.setOnAction(event -> refreshTable());
        searchField.setOnAction(event -> refreshTable());
    }

    public void refreshTable() {
        TaskStatus status = toStatus(filterBox.getValue());
        tableItems.setAll(controller.loadTasks(status, searchField.getText()));
    }

    private void populateForm(TaskDto task) {
        selectedTaskId = task.id();
        titleField.setText(task.title());
        descriptionField.setText(task.description() == null ? "" : task.description());
        dueDatePicker.setValue(task.dueDate());
    }

    private void clearForm() {
        selectedTaskId = null;
        titleField.clear();
        descriptionField.clear();
        dueDatePicker.setValue(null);
        taskTable.getSelectionModel().clearSelection();
    }

    private void saveTask() {
        try {
            TaskDto saved = controller.save(selectedTaskId, titleField.getText(), descriptionField.getText(), dueDatePicker.getValue());
            refreshTable();

            if (!selectTaskInTable(saved.id())) {
                filterBox.setValue(FILTER_ALL);
                searchField.clear();
                refreshTable();
                selectTaskInTable(saved.id());
            }

            clearForm();
        } catch (IllegalArgumentException ex) {
            showError("Validation", ex.getMessage());
        } catch (Exception ex) {
            showError("Save Failed", "Unable to save task.");
        }
    }

    private void deleteTask() {
        if (selectedTaskId == null) {
            showError("Selection Required", "Select a task to delete.");
            return;
        }

        try {
            controller.delete(selectedTaskId);
            refreshTable();
            clearForm();
        } catch (TaskNotFoundException ex) {
            showError("Missing Task", ex.getMessage());
            refreshTable();
            clearForm();
        }
    }

    private void toggleTask() {
        if (selectedTaskId == null) {
            showError("Selection Required", "Select a task to toggle.");
            return;
        }

        TaskDto selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selection Required", "Select a task to toggle.");
            return;
        }

        try {
            boolean nextCompleted = selected.status() != TaskStatus.COMPLETED;
            controller.toggle(selectedTaskId, nextCompleted);
            refreshTable();
        } catch (TaskNotFoundException ex) {
            showError("Missing Task", ex.getMessage());
            refreshTable();
            clearForm();
        }
    }

    private TaskStatus toStatus(String value) {
        if (FILTER_ACTIVE.equals(value)) {
            return TaskStatus.ACTIVE;
        }
        if (FILTER_COMPLETED.equals(value)) {
            return TaskStatus.COMPLETED;
        }
        return null;
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean selectTaskInTable(UUID id) {
        if (id == null) {
            return false;
        }

        for (int i = 0; i < tableItems.size(); i++) {
            TaskDto task = tableItems.get(i);
            if (id.equals(task.id())) {
                taskTable.getSelectionModel().select(i);
                taskTable.scrollTo(i);
                return true;
            }
        }
        return false;
    }
}
