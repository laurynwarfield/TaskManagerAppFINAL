package com.example.taskmanagerapp;

import java.io.*;
import java.util.*;

public class TaskManager {
    private LinkedList<Task> tasks = new LinkedList<>();
    private Queue<Task> taskQueue = new LinkedList<>();

    public void addTask(Task task) {
        tasks.add(task);
        taskQueue.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        taskQueue.remove(task);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
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
        taskQueue.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = Task.fromDataString(line);
                if (task != null) {
                    tasks.add(task);
                    taskQueue.add(task);
                }
            }
        }
    }

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

    public Task processTask() {
        return taskQueue.poll();
    }
}
