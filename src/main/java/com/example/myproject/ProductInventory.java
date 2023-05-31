package com.example.myproject;

public class ProductInventory {
    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getBest_bef() {
        return best_bef;
    }

    public void setBest_bef(int best_bef) {
        this.best_bef = best_bef;
    }

    String Product_name;
    String Price;
    String Quantity;
    String Date;
    int best_bef;

}
