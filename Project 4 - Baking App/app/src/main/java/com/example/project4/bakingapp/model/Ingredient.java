
package com.example.project4.bakingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Ingredient implements Serializable {

    @SerializedName("quantity")
    @Expose
    private Float quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Ingredient() {
    }

    /**
     * 
     * @param measure
     * @param ingredient
     * @param quantity
     */
    public Ingredient(Float quantity, String measure, String ingredient) {
        super();
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("quantity", quantity).append("measure", measure).append("ingredient", ingredient).toString();
    }

}
