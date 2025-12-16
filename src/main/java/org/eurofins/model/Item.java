package org.eurofins.model;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

/**
 * POJO representing an item in a quote
 */
public class Item {
    @SerializedName("item")
    private String item;
    
    @SerializedName("quantity")
    private BigDecimal quantity;
    
    @SerializedName("unitaryPrice")
    private BigDecimal unitaryPrice;
    
    @SerializedName("discountPercentage")
    private BigDecimal discountPercentage;
    
    @SerializedName("discountAmount")
    private BigDecimal discountAmount;
    
    public String getItem() {
        return item;
    }
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public BigDecimal getQuantity() {
        return quantity;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    
    public BigDecimal getUnitaryPrice() {
        return unitaryPrice;
    }
    
    public void setUnitaryPrice(BigDecimal unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
}