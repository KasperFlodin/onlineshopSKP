package com.example.onlineshop.Models;

import java.io.Serializable;

public class Product implements Serializable {
    int id;
    String name;
    String description;
    int price;
    String objectImage;

    // Constructor
    public Product(int id, String name, String description, int price, String objectImage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.objectImage = objectImage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getObjectImage() {
        return objectImage;
    }

    public void setObjectImage(String obejctImage) {
        this.objectImage = obejctImage;
    }
}
