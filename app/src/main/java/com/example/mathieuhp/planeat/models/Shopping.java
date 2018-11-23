package com.example.mathieuhp.planeat.models;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.example.mathieuhp.planeat.fragments.ShoppingFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.TreeMap;

public class Shopping {

    private TreeMap<Ingredient, Pair<Integer, Boolean>> treeMapIngredient;
    private User user;
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference().child("groceryLists");

    public Shopping() {
        user = User.getUserInstance();
        treeMapIngredient = new TreeMap<>();

        // get the user ingredients or create the branch
        firebaseReference.addValueEventListener(new ValueEventListenerShoppingConstruct(this, user.getId(), firebaseReference));
    }

    /* ---- GETTERS ---- */
    public TreeMap<Ingredient, Pair<Integer, Boolean>> getTreeMapIngredient() {
        return treeMapIngredient;
    }


    /* ---- SETTERS ---- */



    /**
     * add an ingredient to the ingredient user list
     * @param ingredient the ingredient to add
     */
    public void addIngedient(Ingredient ingredient) {

        firebaseReference.child(user.getId()).child(ingredient.getId()).child("quantity").setValue("1");
        firebaseReference.child(user.getId()).child(ingredient.getId()).child("checked").setValue(false);

        // add it to the treeMap
        treeMapIngredient.put(ingredient, new Pair<>(1, false));
    }

    /**
     * change the quantity of the ingredient for the user in DB
     * @param ingredient the ingredient to change
     */
    public void updateQuantity(Ingredient ingredient, String quantity) {

        firebaseReference.child(user.getId()).child(ingredient.getId()).child("quantity").setValue(quantity);
    }

    /**
     * change the checked value of the ingredient for the user in DB
     * @param ingredient the ingredient to change
     */
    public void updateChecked(Ingredient ingredient, boolean checked) {

        firebaseReference.child(user.getId()).child(ingredient.getId()).child("checked").setValue(checked);
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


    /* ---- LISTENERS ---- */
    private class ValueEventListenerShoppingConstruct implements ValueEventListener {

        private Shopping shopping;
        private String userId;
        private DatabaseReference firebase;

        private ValueEventListenerShoppingConstruct(Shopping s, String userId, DatabaseReference firebase) {
            this.shopping = s;
            this.userId = userId;
            this.firebase = firebase;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            try {
                // if the user doesn't have a shopping yet, create it in DB
                if (!dataSnapshot.child(userId).exists()) {
                    firebase.child(userId).setValue("-1");
                }

                // store all the ingredients in the treeMap if the quantity is higher than 0
                for (DataSnapshot ds : dataSnapshot.child(userId).getChildren()) {
                    int quantity = Integer.parseInt((String) ds.child("quantity").getValue());
                    boolean checked = (Boolean) ds.child("checked").getValue();
                    if (quantity >= 0) {
                        Ingredient ingredient = Ingredient.ingredientList.get(Integer.parseInt(ds.getKey()));
                        shopping.getTreeMapIngredient().put(ingredient, new Pair<>(quantity, checked));
                    }
                }

                // notify the observers
                if(ShoppingFragment.getShoppingFragment() != null)
                    ShoppingFragment.getShoppingFragment().updateView();

            }  catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    }
}
