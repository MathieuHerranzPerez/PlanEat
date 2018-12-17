package com.example.mathieuhp.planeat.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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
    private String imageLink;
    private Bitmap image;
    private String description;
    private int nbPeople;
    private int calories;
    private int preparationTime;
    private float difficulty;
    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Component> listComponent = new ArrayList<>();
    private ArrayList<String> preparation = new ArrayList<>();
    private boolean isShared;
    private float score;

    private DatabaseReference firebaseReference  = FirebaseDatabase.getInstance().getReference();

    public Recipe() {

    }


    public Recipe(String id) {
        this.id = id;
        // todo get the recipe from DB
        firebaseReference = FirebaseDatabase.getInstance().getReference().child("recipes");
        firebaseReference.addValueEventListener(new ValueEventListenerRecipeConstruct(this));
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

    /* ---- GETTER ---- */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Component> getListComponent() {
        return listComponent;
    }

    public int getNbPeople() {
        return nbPeople;
    }

    public int getCalories() {
        return calories;
    }

    public String getDescription() {
        return description;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public ArrayList<String> getPreparation() {
        return preparation;
    }

    public String getImageLink() {
        return imageLink;
    }

    public Bitmap getImage() {
        return image;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public boolean isShared() {
        return isShared;
    }

    public float getScore() {
        return score;
    }

    /* ---- SETTERS ---- */

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListComponent(ArrayList<Component> listComponent) {
        this.listComponent = listComponent;
    }

    public void setNbPeople(int nbPeople) {
        this.nbPeople = nbPeople;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void setPreparation(ArrayList<String> preparation) {
        this.preparation = preparation;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public void setScore(float score) {
        this.score = score;
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

    //Add the ingredient to the database
    public void addRecipeToDatabase(Recipe recipe) {
        firebaseReference.child("recipes").child(recipe.id).child("name").setValue(recipe.name);
        firebaseReference.child("recipes").child(recipe.id).child("description").setValue(recipe.description);
        firebaseReference.child("recipes").child(recipe.id).child("persons").setValue(recipe.nbPeople);
        firebaseReference.child("recipes").child(recipe.id).child("preparationTime").setValue(recipe.preparationTime);
        firebaseReference.child("recipes").child(recipe.id).child("difficulty").setValue(recipe.difficulty);
        for (int i = 0; i < listComponent.size(); i++) {
            firebaseReference.child("recipe").child(recipe.id).child("ingredient").child(recipe.listComponent.get(i).getIngredient().getId()).setValue(recipe.listComponent.get(i).getQuantity());
            firebaseReference.child("recipe").child(recipe.id).child("ingredient").child(recipe.listComponent.get(i).getIngredient().getId()).setValue(recipe.listComponent.get(i).getUnity());
            firebaseReference.child("recipe").child(recipe.id).child("ingredient").child(recipe.listComponent.get(i).getIngredient().getId()).setValue(recipe.listComponent.get(i).getIngredient());

        }
        for (int b = 0; b < preparation.size(); b++) {
            firebaseReference.child("recipes").child(recipe.id).child("preparation").child(String.valueOf(b)).setValue(recipe.preparation.get(b));
        }
        for (int a = 0; a < tags.size(); a++) {
            firebaseReference.child("recipes").child(recipe.id).child("tags").child(String.valueOf(a)).setValue(recipe.tags.get(a));
        }
        firebaseReference.child("recipes").child(recipe.id).child("isShared").setValue(recipe.isShared);
        firebaseReference.child("recipes").child(recipe.id).child("calories").setValue(recipe.calories);
    }

    //Calculate the calories for 1 ingredient
    public float calculCalorie(Component component) {
        float calories = 0;
        float calorieper100grams = 0;
        float gramme = 0;
        switch (component.getUnity()) {
            case L:
                gramme = component.getQuantity() * 1000;
                break;
            case cl:
                gramme = component.getQuantity() * 10;
                break;
            case ml:
                gramme = component.getQuantity() * 1;
                break;
            case cup:
                gramme = component.getQuantity() * 115;
                break;
            case cuillere:
                gramme = component.getQuantity() * 15;
                break;
            case unite:
                gramme = component.getQuantity() * 60;
                break;
            case kg:
                gramme = component.getQuantity() * 1000;
                break;
        }
        Log.d("test", component.getIngredient().getKiloCaloriesPerHundredGrams());
        String lol = component.getIngredient().getKiloCaloriesPerHundredGrams();
        lol = lol.replace(",",".");
        calorieper100grams = Float.parseFloat(lol);
        calories = (calorieper100grams * gramme ) / 100 ;
        return calories;
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
