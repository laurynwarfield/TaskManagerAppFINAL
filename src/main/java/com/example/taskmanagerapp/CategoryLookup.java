package com.example.taskmanagerapp;

import java.util.*;

public class CategoryLookup {
    private HashMap<String, String> categoryMap = new HashMap<>();

    public CategoryLookup() {
        categoryMap.put("Work", "Work-related tasks and meetings");
        categoryMap.put("Personal", "Personal errands and activities");
        categoryMap.put("Other", "Miscellaneous or uncategorized tasks");
    }

    public String getDescription(String category) {
        return categoryMap.getOrDefault(category, "Unknown category");
    }

    // Added method to provide the list of categories for UI usage
    public List<String> getCategories() {
        return new ArrayList<>(categoryMap.keySet());
    }
}
