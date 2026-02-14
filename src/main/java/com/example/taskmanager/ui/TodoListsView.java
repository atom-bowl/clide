package com.example.taskmanager.ui;

import com.example.taskmanager.domain.TodoItem;
import com.example.taskmanager.domain.TodoList;
import com.example.taskmanager.service.TodoListService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
public class TodoListsView {
    private final TodoListService todoListService;
    private final BorderPane root = new BorderPane();
    private final ListView<TodoList> listsListView = new ListView<>();
    private final VBox itemsBox = new VBox(8);
    private final Label selectedListLabel = new Label("Select a list");

    private TodoList currentList;

    public TodoListsView(TodoListService todoListService) {
        this.todoListService = todoListService;
        buildLayout();
        refreshLists();
    }

    public Parent getRoot() {
        return root;
    }

    private void buildLayout() {
        root.setPadding(new Insets(18));

        VBox leftPanel = new VBox(10);
        leftPanel.setPrefWidth(250);
        leftPanel.setPadding(new Insets(10));

        Label listsLabel = new Label("TODO Lists");
        listsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        HBox listButtons = new HBox(8);
        TextField newListField = new TextField();
        newListField.setPromptText("New list name");
        Button addListButton = new Button("+");
        Button deleteListButton = new Button("Delete");

        addListButton.setOnAction(e -> {
            String name = newListField.getText();
            if (name != null && !name.isBlank()) {
                try {
                    todoListService.createList(name);
                    newListField.clear();
                    refreshLists();
                } catch (Exception ex) {
                    showError("Error", ex.getMessage());
                }
            }
        });

        deleteListButton.setOnAction(e -> {
            TodoList selected = listsListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                todoListService.deleteList(selected.getId());
                currentList = null;
                refreshLists();
                refreshItems();
            }
        });

        listButtons.getChildren().addAll(newListField, addListButton, deleteListButton);
        HBox.setHgrow(newListField, Priority.ALWAYS);

        listsListView.setPrefHeight(400);
        listsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(TodoList list, boolean empty) {
                super.updateItem(list, empty);
                if (empty || list == null) {
                    setText(null);
                } else {
                    setText(list.getName());
                }
            }
        });

        listsListView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            currentList = newVal;
            refreshItems();
        });

        leftPanel.getChildren().addAll(listsLabel, listButtons, listsListView);

        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));

        selectedListLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        HBox itemInputBox = new HBox(8);
        TextField newItemField = new TextField();
        newItemField.setPromptText("Add new item");
        Button addItemButton = new Button("Add");

        addItemButton.setOnAction(e -> {
            String text = newItemField.getText();
            if (currentList != null && text != null && !text.isBlank()) {
                try {
                    todoListService.addItem(currentList.getId(), text);
                    newItemField.clear();
                    refreshLists();
                    refreshItems();
                } catch (Exception ex) {
                    showError("Error", ex.getMessage());
                }
            }
        });

        itemInputBox.getChildren().addAll(newItemField, addItemButton);
        HBox.setHgrow(newItemField, Priority.ALWAYS);

        ScrollPane itemsScroll = new ScrollPane(itemsBox);
        itemsScroll.setFitToWidth(true);
        itemsScroll.setPrefHeight(400);

        rightPanel.getChildren().addAll(selectedListLabel, itemInputBox, itemsScroll);
        VBox.setVgrow(itemsScroll, Priority.ALWAYS);

        HBox content = new HBox(20, leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        root.setCenter(content);
    }

    private void refreshLists() {
        List<TodoList> lists = todoListService.getAllLists();
        listsListView.getItems().setAll(lists);

        if (currentList != null) {
            lists.stream()
                .filter(l -> l.getId().equals(currentList.getId()))
                .findFirst()
                .ifPresent(list -> {
                    currentList = list;
                    listsListView.getSelectionModel().select(list);
                });
        }
    }

    private void refreshItems() {
        itemsBox.getChildren().clear();

        if (currentList == null) {
            selectedListLabel.setText("Select a list");
            return;
        }

        selectedListLabel.setText(currentList.getName());

        for (TodoItem item : currentList.getItems()) {
            HBox itemBox = new HBox(8);
            itemBox.setAlignment(Pos.CENTER_LEFT);
            itemBox.setPadding(new Insets(5));
            itemBox.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-color: #f9f9f9;");

            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(item.isCompleted());
            checkBox.setOnAction(e -> {
                todoListService.toggleItem(item.getId());
                refreshLists();
                refreshItems();
            });

            Label textLabel = new Label(item.getText());
            if (item.isCompleted()) {
                textLabel.setStyle("-fx-strikethrough: true; -fx-text-fill: #888;");
            }

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Button deleteButton = new Button("X");
            deleteButton.setOnAction(e -> {
                todoListService.deleteItem(item.getId());
                refreshLists();
                refreshItems();
            });

            itemBox.getChildren().addAll(checkBox, textLabel, spacer, deleteButton);
            itemsBox.getChildren().add(itemBox);
        }

        if (currentList.getItems().isEmpty()) {
            Label emptyLabel = new Label("No items yet. Add one above!");
            emptyLabel.setStyle("-fx-text-fill: #888;");
            itemsBox.getChildren().add(emptyLabel);
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
        refreshLists();
        refreshItems();
    }
}
