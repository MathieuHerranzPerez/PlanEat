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
    private String imageLink;
    private String tag;
    private boolean isShared;
    private float score;

    public Recipe(int id) {
        this.id = id;
    }

    public Recipe(int id, String name, ArrayList<Component> listComponent, int nbPeople, int calories, float difficulty, String description, int preparationTime, String imageLink, String tag, boolean isShared, float score) {
        this.id = id;
        this.name = name;
        this.listComponent = listComponent;
        this.nbPeople = nbPeople;
        this.calories = calories;
        this.difficulty = difficulty;
        this.description = description;
        this.preparationTime = preparationTime;
        this.imageLink = imageLink;
        this.tag = tag;
        this.isShared = isShared;
        this.score = score;
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

    public float getScore() {
        return score;
    }

    public String getImageLink() {
        return imageLink;
    }
}
