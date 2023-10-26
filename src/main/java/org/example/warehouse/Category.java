package org.example.warehouse;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private final String name;

    private static final Map<String, Category> sameInstanceForSameName = new HashMap<>();

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Category name can't be null");
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1);

        if (sameInstanceForSameName.containsKey(name)) {
            return sameInstanceForSameName.get(name);
        }

        Category category = new Category(name);
        sameInstanceForSameName.put(name, category);
        return category;
    }

    public String getName() {
        return name;
    }
}
