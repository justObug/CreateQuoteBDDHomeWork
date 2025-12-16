package org.eurofins.model;

import java.math.BigDecimal;

/**
 * Builder class for creating Item objects
 */
public class ItemBuilder {
    private String item = "DEFAULT_ITEM";
    private BigDecimal quantity = BigDecimal.ZERO;
    private BigDecimal unitaryPrice = BigDecimal.ZERO;
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    public static ItemBuilder builder() {
        return new ItemBuilder();
    }
    
    public ItemBuilder item(String item) {
        this.item = item;
        return this;
    }
    
    public ItemBuilder quantity(double quantity) {
        this.quantity = new BigDecimal(String.valueOf(quantity));
        return this;
    }
    
    public ItemBuilder quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }
    
    public ItemBuilder unitaryPrice(double unitaryPrice) {
        this.unitaryPrice = new BigDecimal(String.valueOf(unitaryPrice));
        return this;
    }
    
    public ItemBuilder unitaryPrice(BigDecimal unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
        return this;
    }
    
    public ItemBuilder discountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
        return this;
    }
    
    public Item build() {
        Item itemObj = new Item();
        itemObj.setItem(this.item);
        itemObj.setQuantity(this.quantity);
        itemObj.setUnitaryPrice(this.unitaryPrice);
        itemObj.setDiscountPercentage(this.discountPercentage);
        return itemObj;
    }
}