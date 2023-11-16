package com.example.nettas_bakery;

import java.util.List;

public class Order {

    private String orderkey;
    private String userPhone;
    private String totalPrice;
    private String status;
    private List<OrderedFoodItem> orderedFoodItems;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String orderkey, String userPhone, String totalPrice, String status, List<OrderedFoodItem> orderedFoodItems) {
        this.orderkey = orderkey;
        this.userPhone = userPhone;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderedFoodItems = orderedFoodItems;
    }

    public void setOrderkey(String orderkey) {
        this.orderkey = orderkey;
    }

    public String getOrderkey() {
        return orderkey;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderedFoodItem> getOrderedFoodItems() {
        return orderedFoodItems;
    }

    public void setOrderedFoodItems(List<OrderedFoodItem> orderedFoodItems) {
        this.orderedFoodItems = orderedFoodItems;
    }

}
