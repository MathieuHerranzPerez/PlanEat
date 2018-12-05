package com.example.mathieuhp.planeat.models;

public class Component {

    private String recipeId;
    private float quantity;
    private QuantityUnit unit;
    private Ingredient ingredient;

    /**** CONSTRUCTORS ****/

    public Component(String recipeId, float quantity, QuantityUnit unit, Ingredient ingredient) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.unit = unit;
        this.ingredient = ingredient;
    }


    /**** GETTERS ****/

    public String getRecipeId() {
        return recipeId;
    }

    public float getQuantity() {
        return quantity;
    }

    public QuantityUnit getUnit() {
        return unit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }


    /**** SETTERS ****/

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setUnit(QuantityUnit unit) {
        this.unit = unit;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
