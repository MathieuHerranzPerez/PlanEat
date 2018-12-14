package com.example.mathieuhp.planeat.models;

public class Component {

    private String recipeId;
    private float quantity;
    private QuantityUnit unit;
    private Ingredient ingredient;
    private float calories; //calories calculated with quantity

    public Component() {
        this.recipeId = "";
        this.quantity = 0;
        this.unit = QuantityUnit.unit;
        this.ingredient = null;
        this.calories = 0;
    }

    /**** CONSTRUCTORS ****/



    public Component(String recipeId, float quantity, QuantityUnit unit, Ingredient ingredient, float calories) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.unit = unit;
        this.ingredient = ingredient;
        this.calories = 0;
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

    public float getCalories(){
        return calories;
    }

    public String getIngredientName(){
        return ingredient.getName();
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

    public void setCalories(float calories){
        this.calories = calories;
    }
}
