package com.example.mathieuhp.planeat.models;

import android.support.annotation.NonNull;

import com.example.mathieuhp.planeat.fragments.FridgeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Ingredient implements Comparable {

    // the list of all ingredients instantiated
    public static List<Ingredient> ingredientList = new ArrayList<>();

    private String id;
    private String name;
    private String kiloCaloriesPerHundredGrams;
    private String type;

//    private static DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference().child("ingredients");


    public Ingredient() {
        // default constructor required for calls to DataSnapshot.getValue(xxx.class)
    }

//    public Ingredient(String id) {
//        this.id = id;
//
//        // get the information of the ingredient from the database
//        firebaseReference.addListenerForSingleValueEvent(new MyValueEventListener(this));
//        // add the new Ingredient to the list of ingredient
//        Ingredient.ingredientList.add(this);
//    }

    public Ingredient(String id, String name, String kiloCaloriesPerHundredGrams, String type) {
        this.id = id;
        this.name = name;
        this.kiloCaloriesPerHundredGrams = kiloCaloriesPerHundredGrams;
        this.type = type;

        // add the new Ingredient to the list of ingredient
        Ingredient.ingredientList.add(this);
    }

    /* --- GETTERS --- */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKiloCaloriesPerHundredGrams() {
        return kiloCaloriesPerHundredGrams;
    }

    public String getType() {
        return type;
    }

    /* --- SETTERS --- */

    private void setName(String name) {
        this.name = name;
    }

    private void setKiloCaloriesPerHundredGrams(String kiloCaloriesPerHundredGrams) {
        this.kiloCaloriesPerHundredGrams = kiloCaloriesPerHundredGrams;
    }

    private void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Ingredient{\n" +
                " id=" + id + "\'\n" +
                " name='" + name + "\'\n" +
                " kiloCaloriesPerHundredGrams=" + kiloCaloriesPerHundredGrams + "\'\n" +
                " type='" + type + "\'\n" +
                '}';
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(!(o instanceof Ingredient))
            return 0;
        Ingredient i = (Ingredient) o;
        if(this.type.equals(((Ingredient) o).getType()))
            return this.name.compareTo(((Ingredient) o).getName());
        else
            return this.type.compareTo(((Ingredient) o).getType());
    }

//    private class MyValueEventListener implements ValueEventListener {
//
//        private Ingredient ingredient;
//
//
//        private MyValueEventListener(Ingredient ingredient) {
//            this.ingredient = ingredient;
//        }
//
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            try {
//
//                DataSnapshot ds = dataSnapshot.child(ingredient.getId());
//                ingredient.setName(ds.getValue(Ingredient.class).getName());
//                ingredient.setType(ds.getValue(Ingredient.class).getType());
//                ingredient.setKiloCaloriesPerHundredGrams(ds.getValue(Ingredient.class).getKiloCaloriesPerHundredGrams());
//
////                // notify the observers
////                if(FridgeFragment.getFridgeFragment() != null)
////                    FridgeFragment.getFridgeFragment().updateView();
//
//            }  catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {}
//    }
}
