package com.daus.catering.history;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Order {
    private int id;
    private String username;
    private String order_details; // The raw JSON string
    private String order_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderItem> getOrderDetails() {
        // Parse the JSON string into a List of OrderItem objects
        Gson gson = new Gson();
        Type listType = new TypeToken<List<OrderItem>>(){}.getType();
        return gson.fromJson(order_details, listType);
    }

    public String getOrderDate() {
        return order_date;
    }

    public void setOrderDate(String order_date) {
        this.order_date = order_date;
    }
}

class OrderItem {
    private String item;
    private int price;
    private int quantity;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
