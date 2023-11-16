package com.example.nettas_bakery;

public class Food {


    private String key;
    private String name;
    private String image;
    private String ingredients;
    private String price;
    private String portion;
    private String discount;
    private String menuID;

    public Food() {
    }

    public Food(String key, String name, String image, String ingredients, String price, String portion, String discount, String menuID) {
        this.key = key;
        this.name = name;
        this.image = image;
        this.ingredients = ingredients;
        this.price = price;
        this.portion = portion;
        this.discount = discount;
        this.menuID = menuID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }


}
