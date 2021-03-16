package com.example.library.constants;

public enum Category {
    NEW("NEW"),
    CLASSIC("CLASSIC"),
    STANDARD("STANDARD");

    private String category;

    Category(String category) {this.category = category;}

    String getValue() { return category; }

}
