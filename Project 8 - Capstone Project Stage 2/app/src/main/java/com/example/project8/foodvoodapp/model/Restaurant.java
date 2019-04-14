
package com.example.project8.foodvoodapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {

    @SerializedName("budget")
    @Expose
    private String budget;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("menu")
    @Expose
    private List<Menu> menu = null;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("restaurant_name")
    @Expose
    private String restaurantName;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Restaurant() {
    }

    /**
     * 
     * @param budget
     * @param menu
     * @param image
     * @param rating
     * @param restaurantName
     */
    public Restaurant(String budget, String image, List<Menu> menu, String rating, String restaurantName) {
        super();
        this.budget = budget;
        this.image = image;
        this.menu = menu;
        this.rating = rating;
        this.restaurantName = restaurantName;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

}
