package com.example.mathieuhp.planeat.models;

import android.support.annotation.NonNull;

import com.example.mathieuhp.planeat.fragments.FridgeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.TreeMap;

public class Fridge {

    private TreeMap<Ingredient, Integer> treeMapIngredient;
    private User user;
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference().child("fridgeContents");

    public Fridge() {
        user = User.getUserInstance();
        treeMapIngredient = new TreeMap<>();

        // get the user ingredients or create the branch
        firebaseReference.addValueEventListener(new ValueEventListenerFridgeConstruct(this, user.getId(), firebaseReference));
    }

    /* ---- GETTERS ---- */

    public TreeMap<Ingredient, Integer> getTreeMapIngredient() {
        return treeMapIngredient;
    }


    /* ---- SETTERS ---- */

    /**
     * add an ingredient to the ingredient user list
     * @param ingredient the ingredient to add
     */
    public void addIngedient(Ingredient ingredient) {
        firebaseReference.child(user.getId()).child(ingredient.getId()).child("quantity").setValue("1");

        // add it to the treeMap
        treeMapIngredient.put(ingredient, 1);
    }

    /**
     * change the quantity of the ingredient for the user in DB
     * @param ingredient the ingredient to change
     */
    public void updateQuantity(Ingredient ingredient, String quantity) {

        firebaseReference.child(user.getId()).child(ingredient.getId()).child("quantity").setValue(quantity);
    }

    /**
     * Delete the ingredient for the user
     * @param ingredient the ingredient to delete
     */
    public void deleteIngredient(Ingredient ingredient) {
        // remove it from the treeMap
        treeMapIngredient.remove(ingredient);

        // remove it in the user fridge DB
        try {
            this.firebaseReference.child(user.getId()).child(ingredient.getId()).removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ValueEventListenerFridgeConstruct implements ValueEventListener {

        private Fridge fridge;
        private String userId;
        private DatabaseReference firebase;

        private ValueEventListenerFridgeConstruct(Fridge f, String userId, DatabaseReference firebase) {
            this.fridge = f;
            this.userId = userId;
            this.firebase = firebase;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            try {
                // if the user doesn't have a fridge yet, create it in DB
                if (!dataSnapshot.child(userId).exists()) {
                    firebase.child(userId).setValue("-1");
                }

                // store all the ingredients in the treeMap if the quantity is higher than 0
                for (DataSnapshot ds : dataSnapshot.child(userId).getChildren()) {
                    int quantity = Integer.parseInt((String) ds.child("quantity").getValue());
                    if (quantity >= 0) {
                        Ingredient ingredient = Ingredient.ingredientList.get(Integer.parseInt(ds.getKey()));
                        fridge.getTreeMapIngredient().put(ingredient, quantity);
                    }
                }

                // notify the observers
                if(FridgeFragment.getFridgeFragment() != null)
                    FridgeFragment.getFridgeFragment().updateView();

            }  catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    }
}
