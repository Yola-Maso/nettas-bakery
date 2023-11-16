package com.example.nettas_bakery;

public class Category {

    private String name;
    private String image;

    private String key;

    public Category() {
        // Default constructor required for Firebase
    }

    public Category(String name, String image, String key) {
        this.name = name;
        this.image = image;
        this.key = key;
    }


    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}