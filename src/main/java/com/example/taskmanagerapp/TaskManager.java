package com.example.taskmanagerapp;

import java.io.*;
import java.util.*;

public class TaskManager {
    private LinkedList<Task> tasks = new LinkedList<>();
    private Queue<Task> taskQueue = new LinkedList<>();  // Queue for task processing

    public void addTask(Task task) {
        tasks.add(task);  // LinkedList for general task management
        taskQueue.add(task);  // Add to Queue as well for processing
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        taskQueue.remove(task);  // Remove from Queue as well
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);  // Convert LinkedList to List for UI
    }

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Task task : tasks) {
                writer.println(task.toDataString());
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        tasks.clear();
        taskQueue.clear();  // Clear Queue as well when loading new tasks
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromDataString(line);
                if (task != null) {
                    tasks.add(task);
                    taskQueue.add(task);  // Add to Queue as well
                }
            }
        }
    }

    // Sort tasks using Merge Sort by priority
    public void sortTasksByPriority() {
        tasks = (LinkedList<Task>) mergeSort(tasks);
    }

    private List<Task> mergeSort(List<Task> list) {
        if (list.size() <= 1) return list;

        int mid = list.size() / 2;
        List<Task> left = mergeSort(new ArrayList<>(list.subList(0, mid)));
        List<Task> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));

        return merge(left, right);
    }

    private List<Task> merge(List<Task> left, List<Task> right) {
        List<Task> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getPriority() < right.get(j).getPriority()) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));

        return result;
    }

    // Process tasks from the Queue (FIFO)
    public Task processTask() {
        return taskQueue.poll();  // Return the first task in the queue and remove it
    }
}