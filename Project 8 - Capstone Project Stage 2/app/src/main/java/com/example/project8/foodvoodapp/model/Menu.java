
package com.example.project8.foodvoodapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu implements Serializable {

    @SerializedName("amount")
    @Expose
    private float amount;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Menu() {
    }

    /**
     * 
     * @param amount
     * @param itemName
     * @param shortDescription
     * @param image
     */
    public Menu(float amount, String image, String itemName, String shortDescription) {
        super();
        this.amount = amount;
        this.image = image;
        this.itemName = itemName;
        this.shortDescription = shortDescription;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

}
