package com.example.nettas_bakery;

public class OrderedFoodItem {

    private String cKey;
    private String cName;
    private String cPrice;
    private String cQuantity;

    public OrderedFoodItem() {
    }

    public OrderedFoodItem(String cKey, String cName, String cPrice, String cQuantity) {
        this.cKey = cKey;
        this.cName = cName;
        this.cPrice = cPrice;
        this.cQuantity = cQuantity;
    }

    public String getcKey() {
        return cKey;
    }

    public void setcKey(String cKey) {
        this.cKey = cKey;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcPrice() {
        return cPrice;
    }

    public void setcPrice(String cPrice) {
        this.cPrice = cPrice;
    }

    public String getcQuantity() {
        return cQuantity;
    }

    public void setcQuantity(String cQuantity) {
        this.cQuantity = cQuantity;
    }
}
