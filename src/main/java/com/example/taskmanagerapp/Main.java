package com.example.taskmanagerapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {
    private TaskManager taskManager = new TaskManager();
    private TaskBST taskBST = new TaskBST();
    private CategoryLookup categoryLookup = new CategoryLookup();

    private ListView<Task> listView = new ListView<>();
    private Label summaryLabel = new Label("Total: 0 | High Priority: 0");

    @Override
    public void start(Stage primaryStage) {
        TextField titleField = new TextField();
        DatePicker dueDatePicker = new DatePicker();
        TextField priorityField = new TextField();
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Work", "Personal", "Other");
        categoryBox.getSelectionModel().selectFirst();

        TextField searchField = new TextField();
        searchField.setPromptText("Search tasks...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterTasks(newVal));

        Button addButton = new Button("Add Task");
        Button deleteButton = new Button("Delete Selected");
        Button saveButton = new Button("Save to File");
        Button loadButton = new Button("Load from File");
        Button sortButton = new Button("Sort by Priority");
        Button processButton = new Button("Process Next Task");

        addButton.setOnAction(e -> {
            String title = titleField.getText();
            LocalDate date = dueDatePicker.getValue();
            String priorityText = priorityField.getText();
            String category = categoryBox.getValue();

            try {
                int priority = Integer.parseInt(priorityText);
                if (title.isEmpty() || date == null || category == null) {
                    throw new IllegalArgumentException("All fields are required.");
                }
                Task task = new Task(title, date, priority, category);
                taskManager.addTask(task);
                taskBST.insert(task);  // Add to BST
                titleField.clear();
                dueDatePicker.setValue(null);
                priorityField.clear();
                refreshList();
            } catch (NumberFormatException ex) {
                showAlert("Priority must be a number.");
            } catch (IllegalArgumentException ex) {
                showAlert(ex.getMessage());
            }
        });

        deleteButton.setOnAction(e -> {
            Task selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                taskManager.deleteTask(selected);
                refreshList();
            } else {
                showAlert("Please select a task to delete.");
            }
        });

        saveButton.setOnAction(e -> {
            try {
                taskManager.saveToFile("tasks.txt");
                showAlert("Tasks saved to file.");
            } catch (Exception ex) {
                showAlert("Error saving file: " + ex.getMessage());
            }
        });

        loadButton.setOnAction(e -> {
            try {
                taskManager.loadFromFile("tasks.txt");
                refreshList();
                showAlert("Tasks loaded from file.");
            } catch (Exception ex) {
                showAlert("Error loading file: " + ex.getMessage());
            }
        });

        sortButton.setOnAction(e -> {
            taskManager.sortTasksByPriority();
            refreshList();
        });

        processButton.setOnAction(e -> {
            Task task = taskManager.processTask();
            if (task != null) {
                String desc = categoryLookup.getDescription(task.getCategory());
                showAlert("Processing task: " + task.getTitle() + "\n" + desc);
                // Do NOT refresh list here — task stays visible until deleted
            } else {
                showAlert("No tasks to process.");
            }
        });

        VBox inputs = new VBox(5,
                new Label("Title"), titleField,
                new Label("Due Date"), dueDatePicker,
                new Label("Priority (number)"), priorityField,
                new Label("Category"), categoryBox,
                addButton
        );

        HBox controls = new HBox(10, deleteButton, saveButton, loadButton, sortButton, processButton);
        VBox layout = new VBox(10, searchField, inputs, listView, summaryLabel, controls);
        layout.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(layout, 500, 650);

        // Custom cell factory to add color box for priority, without interfering with other styles
        listView.setCellFactory(lv -> new ListCell<Task>() {
            private Rectangle colorBox = new Rectangle(10, 10);

            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(task.toString());
                    colorBox.setFill(getPriorityColor(task.getPriority()));
                    setGraphic(colorBox);

                    // Style overdue tasks background, separate from priority color box
                    if (task.getDueDate().isBefore(LocalDate.now())) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Color getPriorityColor(int priority) {
        if (priority >= 8) return Color.RED;
        else if (priority >= 5) return Color.ORANGE;
        else return Color.GREEN;
    }

    private void refreshList() {
        List<Task> tasks = taskManager.getTasks();
        listView.getItems().setAll(tasks);
        long highPriorityCount = tasks.stream().filter(t -> t.getPriority() >= 7).count();
        summaryLabel.setText("Total: " + tasks.size() + " | High Priority: " + highPriorityCount);
    }

    private void filterTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        listView.getItems().setAll(
                taskManager.getTasks().stream()
                        .filter(t -> t.getTitle().toLowerCase().contains(lowerKeyword) ||
                                t.getCategory().toLowerCase().contains(lowerKeyword))
                        .collect(Collectors.toList())
        );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

