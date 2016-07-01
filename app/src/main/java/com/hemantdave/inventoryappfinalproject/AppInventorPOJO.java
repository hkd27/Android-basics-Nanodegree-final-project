package com.hemantdave.inventoryappfinalproject;

/**
 * Created by INDIA on 6/29/2016.
 */
public class AppInventorPOJO {
    private int id;
    private String name;
    private String quantity;
    private String price;
    private String sales;
    private String email;
    private String imagePath;

    public AppInventorPOJO() {
    }

    public AppInventorPOJO(int id) {
        this.id = id;
    }

    public AppInventorPOJO(String name, String quantity, String price, String sales, String email, String image) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.sales = sales;
        this.email = email;
        this.imagePath = image;
    }


    public AppInventorPOJO(int id, String name, String quantity, String price, String sales, String email) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.sales = sales;
        this.email = email;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String image) {
        this.imagePath = image;
    }
}
