package com.example.myapplication;

public class Product {
    private String name;
    private double price;
    private String image;
    private int quantity;

    public Product(String name, double price, String image, int quantity) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    // Constructor without quantity for backward compatibility
    public Product(String name, double price, String image) {
        this(name, price, image, 10); // Default quantity
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}