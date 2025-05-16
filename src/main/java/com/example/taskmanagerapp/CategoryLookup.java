package com.example.taskmanagerapp;

import java.util.HashMap;
import java.util.Set;

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

    public Set<String> getCategories() {
        return categoryMap.keySet();
    }
}

