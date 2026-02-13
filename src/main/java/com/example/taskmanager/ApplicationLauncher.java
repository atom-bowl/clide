package com.example.taskmanager;

import com.example.taskmanager.ui.MainView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationLauncher extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = new SpringApplicationBuilder(TaskManagerApplication.class)
                .headless(false)
                .run(getParameters().getRaw().toArray(String[]::new));
    }

    @Override
    public void start(Stage stage) {
        MainView mainView = context.getBean(MainView.class);
        Scene scene = new Scene(mainView.getRoot(), 1000, 680);

        stage.setTitle("Task Manager");
        stage.setMinWidth(840);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (context != null) {
            context.close();
        }
        Platform.exit();
    }
}
