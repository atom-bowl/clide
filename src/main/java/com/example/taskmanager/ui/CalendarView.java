package com.example.taskmanager.ui;

import com.example.taskmanager.dto.TaskDto;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Lazy
public class CalendarView {
    private final MainController controller;
    private final BorderPane root = new BorderPane();
    private final GridPane calendarGrid = new GridPane();
    private final Label monthLabel = new Label();
    private final ListView<String> taskListView = new ListView<>();

    private YearMonth currentYearMonth;
    private LocalDate selectedDate;

    public CalendarView(MainController controller) {
        this.controller = controller;
        this.currentYearMonth = YearMonth.now();
        buildLayout();
        updateCalendar();
    }

    public Parent getRoot() {
        return root;
    }

    private void buildLayout() {
        root.setPadding(new Insets(18));

        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER);

        Button prevButton = new Button("<");
        Button nextButton = new Button(">");
        Button todayButton = new Button("Today");

        monthLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        prevButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        nextButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        todayButton.setOnAction(e -> {
            currentYearMonth = YearMonth.now();
            selectedDate = LocalDate.now();
            updateCalendar();
            updateTaskList();
        });

        header.getChildren().addAll(prevButton, monthLabel, nextButton, todayButton);

        calendarGrid.setHgap(5);
        calendarGrid.setVgap(5);
        calendarGrid.setPadding(new Insets(10));
        calendarGrid.setAlignment(Pos.CENTER);

        VBox calendarBox = new VBox(15, header, calendarGrid);
        calendarBox.setAlignment(Pos.TOP_CENTER);

        VBox taskPanel = new VBox(10);
        taskPanel.setPadding(new Insets(10));
        Label taskLabel = new Label("Tasks for selected date:");
        taskLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        taskListView.setPrefHeight(400);
        taskPanel.getChildren().addAll(taskLabel, taskListView);

        HBox content = new HBox(20, calendarBox, taskPanel);
        HBox.setHgrow(calendarBox, Priority.ALWAYS);
        HBox.setHgrow(taskPanel, Priority.ALWAYS);

        root.setCenter(content);
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();

        monthLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < dayNames.length; i++) {
            Label dayLabel = new Label(dayNames[i]);
            dayLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            dayLabel.setMinWidth(80);
            dayLabel.setAlignment(Pos.CENTER);
            calendarGrid.add(dayLabel, i, 0);
        }

        List<TaskDto> allTasks = controller.loadTasks(null, "");
        Map<LocalDate, Long> taskCounts = allTasks.stream()
            .filter(task -> task.dueDate() != null)
            .collect(Collectors.groupingBy(TaskDto::dueDate, Collectors.counting()));

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;

        int daysInMonth = currentYearMonth.lengthOfMonth();
        int row = 1;
        int col = dayOfWeek;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            VBox dayBox = new VBox(3);
            dayBox.setAlignment(Pos.TOP_CENTER);
            dayBox.setPadding(new Insets(5));
            dayBox.setMinSize(80, 70);
            dayBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #f9f9f9;");

            Label dayNumLabel = new Label(String.valueOf(day));
            dayNumLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));

            if (date.equals(LocalDate.now())) {
                dayBox.setStyle("-fx-border-color: #0078d4; -fx-border-width: 2; -fx-background-color: #e7f3ff;");
            }

            if (date.equals(selectedDate)) {
                dayBox.setStyle("-fx-border-color: #0078d4; -fx-border-width: 2; -fx-background-color: #cce5ff;");
            }

            Long count = taskCounts.get(date);
            if (count != null && count > 0) {
                Label countLabel = new Label(count + " task" + (count > 1 ? "s" : ""));
                countLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
                countLabel.setStyle("-fx-text-fill: #0078d4;");
                dayBox.getChildren().addAll(dayNumLabel, countLabel);
            } else {
                dayBox.getChildren().add(dayNumLabel);
            }

            LocalDate finalDate = date;
            dayBox.setOnMouseClicked(e -> {
                selectedDate = finalDate;
                updateCalendar();
                updateTaskList();
            });

            calendarGrid.add(dayBox, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private void updateTaskList() {
        if (selectedDate == null) {
            taskListView.getItems().clear();
            return;
        }

        List<TaskDto> tasks = controller.loadTasks(null, "");
        List<String> tasksForDate = tasks.stream()
            .filter(task -> selectedDate.equals(task.dueDate()))
            .map(task -> String.format("[%s] %s", task.status(), task.title()))
            .collect(Collectors.toList());

        taskListView.getItems().setAll(tasksForDate);

        if (tasksForDate.isEmpty()) {
            taskListView.getItems().add("No tasks for this date");
        }
    }

    public void refresh() {
        updateCalendar();
        if (selectedDate != null) {
            updateTaskList();
        }
    }
}
