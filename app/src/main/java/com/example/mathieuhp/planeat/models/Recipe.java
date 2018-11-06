package com.example.mathieuhp.planeat.models;

import java.util.ArrayList;

public class Recipe {

    private int id;
    private String name;
    private ArrayList<Component> listComponent;
    private int nbPeople;
    private int calories;
    private float difficulty;
    private String description;
    private int preparationTime;
    private String tag;
    private boolean isShared;
    private float score;


    public Recipe(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public String getTag() {
        return tag;
    }
}
