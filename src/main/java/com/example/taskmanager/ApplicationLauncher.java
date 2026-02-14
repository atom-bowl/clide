package com.example.taskmanager;

import com.example.taskmanager.ui.MainView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApplicationLauncher extends Application {
    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        initializeDataDirectory();
        context = new SpringApplicationBuilder(TaskManagerApplication.class)
                .headless(false)
                .run(getParameters().getRaw().toArray(String[]::new));
    }

    private void initializeDataDirectory() {
        String configuredDir = System.getProperty("clide.data-dir");
        Path dataDir = configuredDir == null || configuredDir.isBlank()
                ? Path.of(System.getProperty("user.home"), ".clide")
                : Path.of(configuredDir);
        try {
            Files.createDirectories(dataDir);
            System.setProperty("clide.data-dir", dataDir.toAbsolutePath().toString().replace('\\', '/'));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to initialize data directory: " + dataDir, e);
        }
    }

    @Override
    public void start(Stage stage) {
        MainView mainView = context.getBean(MainView.class);
        Scene scene = new Scene(mainView.getRoot(), 1000, 680);

        stage.setTitle("Clide Desktop");
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
