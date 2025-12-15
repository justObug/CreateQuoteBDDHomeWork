package org.example.model;

/**
 * Builder class for creating Item objects
 */
public class ItemBuilder {
    private String item = "DEFAULT_ITEM";
    private double quantity = 0.0;
    private double unitaryPrice = 0.0;
    private float discountPercentage = 0.0f;
    
    public static ItemBuilder builder() {
        return new ItemBuilder();
    }
    
    public ItemBuilder item(String item) {
        this.item = item;
        return this;
    }
    
    public ItemBuilder quantity(double quantity) {
        this.quantity = quantity;
        return this;
    }
    
    public ItemBuilder unitaryPrice(double unitaryPrice) {
        this.unitaryPrice = unitaryPrice;
        return this;
    }
    
    public ItemBuilder discountPercentage(float discountPercentage) {
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