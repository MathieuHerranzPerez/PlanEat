package com.example.mathieuhp.planeat.models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.mathieuhp.planeat.fragments.PlanningFragment;
import com.example.mathieuhp.planeat.fragments.RecipesListFragment;
import com.example.mathieuhp.planeat.fragments.UserFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private FirebaseDataRetriever firebaseDataRetriever;

    private String id;
    private String name;
    private ArrayList<Component> listComponent;
    private int nbPeople;
    private int calories;
    private float difficulty;
    private String description;
    private int preparationTime;
    private String imageLink;
    private Bitmap image;
    private List<String> tags;
    private boolean isShared;
    private float score;

    private DatabaseReference firebaseReference;

    public Recipe(FirebaseDataRetriever firebaseDataRetriever, String id) {
        this.firebaseDataRetriever = firebaseDataRetriever;
        this.id = id;
    }


    public Recipe() {

    }

    public Recipe(String id, String name, ArrayList<Component> listComponent, int nbPeople, int calories, float difficulty, String description, int preparationTime, String imageLink, String tag, Bitmap image, List<String> tags, boolean isShared, float score) {
        this.id = id;
        this.name = name;
        this.listComponent = listComponent;
        this.nbPeople = nbPeople;
        this.calories = calories;
        this.difficulty = difficulty;
        this.description = description;
        this.preparationTime = preparationTime;
        this.imageLink = imageLink;
        this.image = image;
        this.tags = tags;
        this.isShared = isShared;
        this.score = score;
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

    public float getScore() {
        return score;
    }

    public String getImageLink() {
        return imageLink;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public void loadInformation() {
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
    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
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
                recipe.setIsShared(Boolean.valueOf((String) ds.child("isShared").getValue()));

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

    public void deleteData() {
        // TODO delete the recipe

        // if the recipe isShared, delete it from the recipeCatalogs
        if(isShared) {
            // TODO firebase rules ?
        }

        // delete it from the recipe schedule
        // TODO firebase rules ?


        // delete it


        if(UserFragment.getUserFragment() != null) {
            UserFragment.getUserFragment().updateView();
        }
    }
}
