package com.example.taskmanagerapp;

public class TaskBST {
    private class Node {
        Task task;
        Node left, right;

        Node(Task task) {
            this.task = task;
        }
    }

    private Node root;

    public void insert(Task task) {
        root = insertRec(root, task);
    }

    private Node insertRec(Node node, Task task) {
        if (node == null) return new Node(task);
        if (task.getTitle().compareToIgnoreCase(node.task.getTitle()) < 0)
            node.left = insertRec(node.left, task);
        else
            node.right = insertRec(node.right, task);
        return node;
    }

    public Task searchByTitle(String title) {
        return searchRec(root, title);
    }

    private Task searchRec(Node node, String title) {
        if (node == null) return null;
        int cmp = title.compareToIgnoreCase(node.task.getTitle());
        if (cmp == 0) return node.task;
        return cmp < 0 ? searchRec(node.left, title) : searchRec(node.right, title);
    }
}
