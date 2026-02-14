package com.example.taskmanager.ui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class MainView {
    private final VBox root = new VBox();
    private final TasksView tasksView;
    private final CalendarView calendarView;
    private final TodoListsView todoListsView;
    private final NotesView notesView;

    public MainView(TasksView tasksView, CalendarView calendarView, TodoListsView todoListsView, NotesView notesView) {
        this.tasksView = tasksView;
        this.calendarView = calendarView;
        this.todoListsView = todoListsView;
        this.notesView = notesView;
        buildLayout();
    }

    public Parent getRoot() {
        return root;
    }

    private void buildLayout() {
        Label header = new Label("Clide");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        header.setPadding(new Insets(18, 18, 10, 18));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tasksTab = new Tab("Tasks", tasksView.getRoot());
        Tab calendarTab = new Tab("Calendar", calendarView.getRoot());
        Tab todoListsTab = new Tab("TODO Lists", todoListsView.getRoot());
        Tab notesTab = new Tab("Notes", notesView.getRoot());

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tasksTab) {
                tasksView.refreshTable();
            } else if (newTab == calendarTab) {
                calendarView.refresh();
            } else if (newTab == todoListsTab) {
                todoListsView.refresh();
            } else if (newTab == notesTab) {
                notesView.refresh();
            }
        });

        tabPane.getTabs().addAll(tasksTab, calendarTab, todoListsTab, notesTab);

        root.getChildren().addAll(header, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }
}
