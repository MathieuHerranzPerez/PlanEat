package com.example.mathieuhp.planeat.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

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

public class Recipe implements Parcelable{

    private FirebaseDataRetriever firebaseDataRetriever;

    private String id;
    private String name;
    private ArrayList<Component> listComponent;
    private int nbPeople;
    private int calories;
    private String description;
    private int preparationTime;
    private float difficulty;
    private ArrayList<ArrayList> ingredients;
    private ArrayList<String> preparation;
    private String imageLink;
    private Bitmap image;
    private List<String> tags;
    private boolean isShared;
    private float score;

    private DatabaseReference firebaseReference;

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

    public Recipe(String name, int nbPeople, String description, int preparationTime, float difficulty, ArrayList<ArrayList> ingredients, ArrayList<String> preparation, boolean isShared) {

        this.name = name;
        this.nbPeople = nbPeople;
        this.description = description;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.preparation = preparation;
        this.isShared = isShared;
        this.score = score;
    }

    public Recipe(FirebaseDataRetriever firebaseDataRetriever, String id) {
        this.id = id;
        this.name = "";
        this.nbPeople = 0;
        this.calories = 0;
        this.difficulty = 0;
        this.description = "";
        this.preparationTime = 0;
        this.imageLink = "";
        this.isShared = false;
        this.score = 0;

        this.firebaseDataRetriever = firebaseDataRetriever;
        // todo get the recipe from DB
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("recipes");
        firebaseReference.addValueEventListener(new ValueEventListenerRecipeConstruct(this));
    }


    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        nbPeople = in.readInt();
        calories = in.readInt();
        description = in.readString();
        preparationTime = in.readInt();
        difficulty = in.readFloat();
        preparation = in.createStringArrayList();
        imageLink = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        tags = in.createStringArrayList();
        isShared = in.readByte() != 0;
        score = in.readFloat();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

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


    //parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(nbPeople);
        dest.writeInt(calories);
        dest.writeString(description);
        dest.writeInt(preparationTime);
        dest.writeFloat(difficulty);
        dest.writeStringList(preparation);
        dest.writeString(imageLink);
        dest.writeParcelable(image, flags);
        dest.writeStringList(tags);
        dest.writeByte((byte) (isShared ? 1 : 0));
        dest.writeFloat(score);
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
                String recipeName;
                if ((recipeName = (String) ds.child("name").getValue()) != null) {
                    recipe.setName(recipeName);
                    Log.d("recipe name", recipeName);
                }
                Integer calories;
                if ((calories = Integer.parseInt((String) ds.child("calories").getValue())) != null) {
                    recipe.setCalories(calories);
                    Log.d("recipe calories", calories.toString());
                }
                Integer nbPeople;
                if ((nbPeople = Integer.parseInt((String) ds.child("persons").getValue())) != null) {
                    recipe.setNbPeople(nbPeople);
                    Log.d("recipe nbPeople", nbPeople.toString());
                }
                String description;
                if ((description = (String) ds.child("preparation").getValue()) != null){
                    recipe.setDescription(description);
                    Log.d("recipe description", description);
                }
                Integer preparationTime;
                if((preparationTime = Integer.parseInt((String)ds.child("preparationTime").getValue())) != null){
                    recipe.setPreparationTime(preparationTime);
                    Log.d("recipe preparation time", preparationTime.toString());
                }
                Float score;
                if((score = Float.parseFloat((String)ds.child("score").getValue())) != null) {
                    recipe.setScore(score);
                    Log.d("recipe score", score.toString());
                }
                Boolean isShared;
                /*if((isShared = Boolean.valueOf((String) ds.child("isShared").getValue())) != null) {
                    recipe.setIsShared(isShared);
                    Log.d("is recipe shared", String.valueOf(isShared));
                }*/

                // todo get the tags


                // notify the observers
                if(PlanningFragment.getPlanningFragment() != null) {
                    PlanningFragment.getPlanningFragment().updateView();
                }
                Log.d("Getting recipe done! ", this.recipe.name);
                //notify that recipe has been retrieved from db
                this.recipe.firebaseDataRetriever.retrieveData();

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
