package com.example.nettas_bakery;

public class CartItem {

    private String ckey;
    private String cName;
    private String cPrice;
    private String cPortion;
    private String cDiscount;
    private int cQuantity;

    public CartItem() {
    }

    public CartItem(String ckey, String cName, String cPrice, String cPortion, String cDiscount, int cQuantity) {
        this.ckey = ckey;
        this.cName = cName;
        this.cPrice = cPrice;
        this.cPortion = cPortion;
        this.cDiscount = cDiscount;
        this.cQuantity = cQuantity;
    }

    public int getcQuantity() {
        return cQuantity;
    }

    public void setcQuantity(int cQuantity) {
        this.cQuantity = cQuantity;
    }



    public String getCkey() {
        return ckey;
    }

    public void setCKey(String ckey) {
        this.ckey = ckey;
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

    public String getcPortion() {
        return cPortion;
    }

    public void setcPortion(String cPortion) {
        this.cPortion = cPortion;
    }

    public String getcDiscount() {
        return cDiscount;
    }

    public void setcDiscount(String cDiscount) {
        this.cDiscount = cDiscount;
    }
}
