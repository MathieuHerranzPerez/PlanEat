package com.example.mathieuhp.planeat.models;

public class Component {


    /* ATTRIBUTS */
    private Recipe recipe;

    private Ingredient ingredient;
    private float quantity;
    private QuantityUnit unity;


    /* CONSTRUCTOR */
    public Component() {
    }

    /* GETTERS */
    public Recipe getRecipe() {
        return recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public QuantityUnit getUnity() {
        return unity;
    }


    /* SETTERS */
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setUnity(QuantityUnit unity) {
        this.unity = unity;
    }

}
