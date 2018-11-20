package com.example.mathieuhp.planeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

public class RecipeFragment extends Fragment implements Updatable {

    final AtomicInteger count = new AtomicInteger();
    LinearLayout section_ingredient;
    private View rootview;
    private Context myContext = getActivity();
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference().child("recipes");
    private int nbIngredient;
    private int nbStep;
    private String id = "5";

    private Recipe recipe;

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_recipe, null);

        firebaseReference.child("recipes").child(id).child("ingredient").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                nbIngredient = 0;
                nbIngredient = count.incrementAndGet() + 1;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        firebaseReference.child("recipes").child(id).child("preparation").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                nbStep = 0;
                nbStep = count.incrementAndGet() + 1;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        firebaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String recipeName = String.valueOf(dataSnapshot.child(id).child("name").getValue());
                TextView text_name = rootview.findViewById(R.id.name);
                text_name.setText(recipeName);

                String recipeDescription = String.valueOf(dataSnapshot.child(id).child("description").getValue());
                TextView text_desc = rootview.findViewById(R.id.description);
                text_desc.setText(recipeDescription);

                String recipeNbPeople = String.valueOf(dataSnapshot.child(id).child("persons").getValue());
                TextView text_nbPeople = rootview.findViewById(R.id.show_nbPerson);
                text_nbPeople.setText(recipeNbPeople);

                String recipePreparationTime = String.valueOf(dataSnapshot.child(id).child("preparationTime").getValue());
                TextView text_prepTime = rootview.findViewById(R.id.show_preparationTime);
                text_prepTime.setText(recipePreparationTime);

                String recipeDifficulty = String.valueOf(dataSnapshot.child(id).child("difficulty").getValue());
                TextView text_difficulty = rootview.findViewById(R.id.show_difficulty);
                text_difficulty.setText(recipeDifficulty);

                section_ingredient = rootview.findViewById(R.id.section_ingredients);
                //TODO : Load ingredient and steps
                //Not working, the constructor of the linearLayout create a bug

                //for (int i = 0; i < nbIngredient; i++) {


/*
                //LinearLayout addIngredient = new LinearLayout(myContext);

                LinearLayout.LayoutParams paramQty = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                addIngredient.setLayoutParams(paramQty);

/*
                    LinearLayout ingredientLayout = new LinearLayout(myContext);
                    ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
                    section_ingredient.addView(ingredientLayout);

                    TextView text_quantity = new EditText(myContext);
//                    TextView text_unity  = new EditText(myContext);
//                    TextView text_ingredient  = new EditText(myContext);

                    ingredientLayout.addView(text_quantity);
//                    ingredientLayout.addView(text_unity);
//                    ingredientLayout.addView(text_ingredient);

                    String recipeQuantityIng = String.valueOf(dataSnapshot.child(id).child("ingredient").child(String.valueOf(0)).child("0").getValue());
//                    String recipeUnityIng = String.valueOf(dataSnapshot.child(id).child("ingredient").child(String.valueOf(0)).child("1").getValue());
//                    String recipeIngredient = String.valueOf(dataSnapshot.child(id).child("ingredient").child(String.valueOf(0)).child("2").getValue());

                    text_quantity.setText(recipeQuantityIng);
//                    text_unity.setText(recipeUnityIng);
//                    text_ingredient.setText(recipeIngredient);

                    LinearLayout.LayoutParams paramQty = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    text_quantity.setLayoutParams(paramQty);
//                    LinearLayout.LayoutParams paramUnity = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
//                    text_unity.setLayoutParams(paramUnity);
//                    LinearLayout.LayoutParams paramIng = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
//                    text_ingredient.setLayoutParams(paramIng);
               // }
               */
                /*LinearLayout section_preparation = rootview.findViewById(R.id.section_preparation);
                for (int i = 0; i < nbStep; i++) {
                    LinearLayout preparationLayout = new LinearLayout(myContext);
                    preparationLayout.setOrientation(LinearLayout.HORIZONTAL);
                    section_preparation.addView(preparationLayout);

                    TextView text_step = new EditText(myContext);

                    ingredientLayout.addView(text_step);

                    String recipeStep = String.valueOf(dataSnapshot.child(id).child("preparation").child(String.valueOf(i)).getValue());

                    text_step.setText(recipeStep);
                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return rootview;
    }

    @Override
    public void updateView() {
        // TODO
    }


}