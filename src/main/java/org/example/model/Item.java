package org.example.model;

import com.google.gson.annotations.SerializedName;

/**
 * POJO representing an item in a quote
 */
public class Item {
    @SerializedName("item")
    private String item;
    
    @SerializedName("quantity")
    private double quantity;
    
    @SerializedName("unitaryPrice")
    private double unitaryPrice;
    
    @SerializedName("discountPercentage")
    private float discountPercentage; // Using primitive float, defaults to 0.0
    
    public String getItem() {
        return item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public double getUnitaryPrice() {
        return unitaryPrice;
    }
    
    public void setUnitaryPrice(double unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }
    
    public float getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}