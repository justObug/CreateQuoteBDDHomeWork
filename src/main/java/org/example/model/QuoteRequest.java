package org.example.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing a quote request
 */
public class QuoteRequest {
    @SerializedName("customer")
    private String customer;
    
    @SerializedName("items")
    private List<Item> items = new ArrayList<>();
    
    public String getCustomer() {
        return customer;
    }
    
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    public List<Item> getItems() {
        return items;
    }
    
    public void setItems(List<Item> items) {
        this.items = items;
    }
}