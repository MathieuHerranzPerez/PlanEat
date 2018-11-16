package com.example.mathieuhp.planeat.models;

import android.support.annotation.NonNull;

import com.example.mathieuhp.planeat.fragments.PlanningFragment;
import com.example.mathieuhp.planeat.fragments.RecipesListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Recipe {

    private String id;
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

    private DatabaseReference firebaseReference;


    public Recipe() {

    }

    public Recipe(String id) {
        this.id = id;

        // todo get the recipe from DB
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("recipes");
        firebaseReference.addValueEventListener(new ValueEventListenerRecipeConstruct(this));
    }

    public String getId() {
        return this.id;
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

    public int getCalories() {
        return calories;
    }

    /* ---- SETTERS ---- */
    private void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setNbPeople(int nbPeople) {
        this.nbPeople = nbPeople;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setScore(float score) {
        this.score = score;
    }


    @Override
    public String toString() {
        return name;
    }

    private class ValueEventListenerRecipeConstruct implements ValueEventListener {

        private Recipe recipe;

        private ValueEventListenerRecipeConstruct(Recipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            try {
                DataSnapshot ds = dataSnapshot.child(recipe.getId());
                recipe.setName((String) ds.child("name").getValue());
                recipe.setCalories(Integer.parseInt((String)ds.child("calories").getValue()));
                recipe.setNbPeople(Integer.parseInt((String) ds.child("persons").getValue()));
                recipe.setDescription((String) ds.child("preparation").getValue());
                recipe.setPreparationTime(Integer.parseInt((String)ds.child("preparationTime").getValue()));
                recipe.setScore(Float.parseFloat((String)ds.child("score").getValue()));

                // todo get the tags


                // notify the observers
                if(PlanningFragment.getPlanningFragment() != null)
                    PlanningFragment.getPlanningFragment().updateView();
                if(RecipesListFragment.getRecipesListFragment() != null)
                    RecipesListFragment.getRecipesListFragment().updateView();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    }
}
