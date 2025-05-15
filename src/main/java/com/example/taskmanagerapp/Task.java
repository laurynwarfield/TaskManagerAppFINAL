package com.example.taskmanagerapp;

import java.time.LocalDate;

public class Task {
    private String title;
    private LocalDate dueDate;
    private int priority;
    private String category;

    public Task(String title, LocalDate dueDate, int priority, String category) {
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getPriority() {
        return priority;
    }

    public String getCategory() {
        return category;
    }

    public String toDataString() {
        return title + "|" + dueDate + "|" + priority + "|" + category;
    }

    public static Task fromDataString(String data) {
        try {
            String[] parts = data.split("\\|");
            if (parts.length != 4) return null;

            String title = parts[0];
            LocalDate date = LocalDate.parse(parts[1]);
            int priority = Integer.parseInt(parts[2]);
            String category = parts[3];

            return new Task(title, date, priority, category);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return title + " (Due: " + dueDate + ", Priority: " + priority + ", Category: " + category + ")";
    }
}
