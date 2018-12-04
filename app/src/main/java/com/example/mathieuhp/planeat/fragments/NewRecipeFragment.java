package com.example.mathieuhp.planeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Component;
import com.example.mathieuhp.planeat.models.Ingredient;
import com.example.mathieuhp.planeat.models.QuantityUnit;
import com.example.mathieuhp.planeat.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class NewRecipeFragment extends Fragment {

    final AtomicInteger count = new AtomicInteger();

    private Context myContext = getActivity();
    private View rootview;

    private View.OnClickListener addIngredient_listener;
    private View.OnClickListener addStep_listener;
    private View.OnClickListener validation_listener;

    private boolean isPut = false;
    private boolean isDifficultyValid;
    private boolean isComplete;

    public Recipe newRecipe;
    public Component newComponent;
    public Ingredient newIngredient;

    public String id;
    public String name;
    public String imageLink;
    public String description;
    public String tag;
    public int nbPeople;
    public int preparationTime;
    public float difficulty;
    public int calories;
    public boolean isShared;
    public Spinner unity1;
    public Spinner unity2;
    public ArrayList<ArrayList> ingredients;
    public ArrayList<String> preparation;
    public LinearLayout sectionIngredient;
    public int nbIngredient = 2;
    public FloatingActionButton buttonAddIngredient;
    public LinearLayout sectionPreparation;
    public int nbStep = 1;
    public FloatingActionButton buttonAddStep;
    public FloatingActionButton validation;
    int nbRecipe = 0;
    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();

    //button to add a new ingredient in the recipe
    {
        addIngredient_listener = new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nbIngredient += 1;

                //Linear Layout
                LinearLayout addIngredient = new LinearLayout(myContext);
                addIngredient.setOrientation(LinearLayout.HORIZONTAL);
                sectionIngredient.addView(addIngredient);

                //EditText for quantity
                EditText newQty = new EditText(myContext);
                addIngredient.addView(newQty);
                newQty.setTag("edit_quantity_" + nbIngredient);
                newQty.setHint(R.string.hint_quantity);
                newQty.setInputType(2);
                LinearLayout.LayoutParams paramQty = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                newQty.setLayoutParams(paramQty);

                //Drop-down
                Spinner dropdown_unity = new Spinner(myContext);
                addIngredient.addView(dropdown_unity);
                dropdown_unity.setTag("dropdown_unity_" + nbIngredient);
                dropdown_unity.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
                LinearLayout.LayoutParams paramUnity = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dropdown_unity.setLayoutParams(paramUnity);

                //EditText for ingredient
                EditText newIngredient = new EditText(myContext);
                addIngredient.addView(newIngredient);
                newIngredient.setTag("edit_ingredient_" + nbIngredient);
                newIngredient.setHint(R.string.hint_ingredient);
                newIngredient.setInputType(1);
                LinearLayout.LayoutParams paramIngredient = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4f);
                newIngredient.setLayoutParams(paramIngredient);

            }
        };
    }

    //button to add a step on the recipe
    {
        addStep_listener = new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nbStep += 1;
                EditText newStep = new EditText(myContext);
                sectionPreparation.addView(newStep);

                //Edittext for next step
                newStep.setTag("edit_step_" + nbStep);
                newStep.setHint(R.string.hint_otherStep);
                newStep.setInputType(1);
            }
        };
    }

    //button create the recipe
    {
        validation_listener = new ImageButton.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                isDifficultyValid = true;
                isComplete = true;

                //Generating the ID of the recipe

                //Set the recipe name and it description
                EditText edit_name = rootview.findViewById(R.id.name);
                if (edit_name.getText().length() == 0) {
                    isComplete = false;
                }
//                else newRecipe.setName(edit_name.getText().toString());

                EditText edit_desc = rootview.findViewById(R.id.description);
                if (edit_desc.getText().length() == 0) {
                    isComplete = false;
                }
//                else newRecipe.setDescription(edit_desc.getText().toString());


                //Set the picture
                //TODO Load picture

                //TODO beta : calculate calories for 1 person
                calories = 12;
                // somme des calorie de chaques ingredients / le nombre de personnes

                //TODO Beta : Set the tags
                tag = "tag";


                //set the nb of people, the time and the difficulty
                EditText edit_nbPerson = rootview.findViewById(R.id.edit_nbPerson);
                if (edit_nbPerson.getText().length() == 0) {
                    //edit.setError("Veuillez renseigner la nombre de personnes");
                    isComplete = false;
                } else {
                    nbPeople = Integer.parseInt(edit_nbPerson.getText().toString());
                }

                EditText edit_prepTime = rootview.findViewById(R.id.edit_preparationTime);
                if (edit_prepTime.getText().length() == 0) {
                    //edit.setError("Veuillez renseigner le temps de préparation");
                    isComplete = false;
                } else {
                    preparationTime = Integer.parseInt(edit_prepTime.getText().toString());
                }

                EditText edit_difficulty = rootview.findViewById(R.id.edit_difficulty);
                if (edit_difficulty.getText().length() == 0) {
                    //edit.setError("Veuillez renseigner la difficulté");
                    isComplete = false;
                } else {
                    int dif = Integer.parseInt(edit_difficulty.getText().toString());
                    if (dif >= 0 && dif <= 5) {
                        difficulty = dif;
                    } else {
                        isDifficultyValid = false;
                    }
                }

                EditText edit_qtyIngredient1 = rootview.findViewById(R.id.edit_quantity_1);
                EditText edit_ingredient1 = rootview.findViewById(R.id.edit_ingredient_l);

                EditText edit_qtyIngredient2 = rootview.findViewById(R.id.edit_quantity_2);
                EditText edit_ingredient2 = rootview.findViewById(R.id.edit_ingredient_2);

                if (edit_qtyIngredient1.getText().length() == 0 || edit_qtyIngredient2.getText().length() == 0 ||
                        edit_ingredient1.getText().length() == 0 || edit_ingredient2.getText().length() == 0) {
                    //edit.setError("Veuillez renseigner la difficulté");
                    isComplete = false;
                } else {
                    ingredients = new ArrayList<>();

                    ArrayList<String> ingredient1 = new ArrayList<>();
                    ingredient1.add(edit_qtyIngredient1.getText().toString());
                    ingredient1.add(unity1.getSelectedItem().toString());
                    ingredient1.add(edit_ingredient1.getText().toString());

                    ArrayList<String> ingredient2 = new ArrayList<>();
                    ingredient2.add(edit_qtyIngredient2.getText().toString());
                    ingredient2.add(unity2.getSelectedItem().toString());
                    ingredient2.add(edit_ingredient2.getText().toString());

                    ingredients.add(ingredient1);
                    ingredients.add(ingredient2);

                    for (int i = 3; i <= nbIngredient; i++) {

                        EditText edit_qtyIngredient = rootview.findViewWithTag("edit_quantity_" + i);
                        String string_edit_quantity = edit_qtyIngredient.getText().toString().trim();
                        Spinner unity = rootview.findViewWithTag("dropdown_unity_" + i);
                        EditText edit_ingredient = rootview.findViewWithTag("edit_ingredient_" + i);
                        String string_edit_ingredient = edit_ingredient.getText().toString().trim();

                        if (nbIngredient > 2 && (!string_edit_quantity.isEmpty() || !string_edit_ingredient.isEmpty())) {

                            ArrayList<String> ingredient = new ArrayList<>();
                            ingredient.add(edit_qtyIngredient.getText().toString());
                            ingredient.add(unity.getSelectedItem().toString());
                            ingredient.add(edit_ingredient.getText().toString());
                            ingredients.add(ingredient);

                        }
                    }
                }

                EditText edit_preparation = rootview.findViewById(R.id.edit_step1);
                if (edit_preparation.getText().length() == 0) {
                    //edit.setError("Veuillez renseigner la difficulté");
                    isComplete = false;
                } else {
                    preparation = new ArrayList<>();

                    preparation.add(edit_preparation.getText().toString());
                    for (int i = 2; i <= nbStep; i++) {
                        EditText edit_step = rootview.findViewWithTag("edit_step_" + i);
                        String step = edit_step.getText().toString().trim();
                        if (nbStep > 1 && !step.isEmpty()) {
                            preparation.add(edit_step.getText().toString());
                        }
                    }
                }

                //Recipe not shared yet
                isShared = false;

                //Verifying if the recipe is complete
                if (!isDifficultyValid) {
                    Snackbar snackbarDifficulty = Snackbar.make(rootview, R.string.invalid_difficulty, Snackbar.LENGTH_SHORT);
                    snackbarDifficulty.show();
                } else if (!isComplete) {
                    Snackbar snackbarComplete = Snackbar.make(rootview, R.string.invalid_completion, Snackbar.LENGTH_SHORT);
                    snackbarComplete.show();
                } else if (isPut) {
                    Snackbar snackbarComplete = Snackbar.make(rootview, "Recette déja soumise", Snackbar.LENGTH_SHORT);
                    snackbarComplete.show();
                } else {
                    Recipe recipe = new Recipe(name, nbPeople, description, preparationTime,
                            difficulty, ingredients, preparation, isShared);

                    firebaseReference.child("recipes").child(id).child("name").setValue(name);
                    firebaseReference.child("recipes").child(id).child("description").setValue(description);
                    firebaseReference.child("recipes").child(id).child("persons").setValue(nbPeople);
                    firebaseReference.child("recipes").child(id).child("preparationTime").setValue(preparationTime);
                    firebaseReference.child("recipes").child(id).child("difficulty").setValue(difficulty);
                    for (int i = 0; i < ingredients.size(); i++) {
                        firebaseReference.child("recipes").child(id).child("ingredient").child(String.valueOf(i)).setValue(ingredients.get(i));
                    }
                    for (int j = 0; j < preparation.size(); j++) {
                        firebaseReference.child("recipes").child(id).child("preparation").child(String.valueOf(j)).setValue(preparation.get(j));
                    }
                    firebaseReference.child("recipes").child(id).child("isShared").setValue(isShared);
                    firebaseReference.child("recipes").child(id).child("calories").setValue(calories);
                    firebaseReference.child("recipes").child(id).child("tag").setValue(tag);

                    Snackbar snackbarComplete = Snackbar.make(rootview, "Recette envoyée", Snackbar.LENGTH_SHORT);
                    snackbarComplete.show();

                    isPut = true;

                }

                //TODO if time : Add a pop up to be sure


            }
        };
    }

    public NewRecipeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newRecipe = new Recipe();
        newComponent = new Component();
        newIngredient = new Ingredient();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_new_recipe, null);

        firebaseReference.child("recipes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                nbRecipe = 0;
                nbRecipe = count.incrementAndGet() + 1;
                id = String.valueOf(nbRecipe);

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

        unity1 = (Spinner) rootview.findViewById(R.id.unity_1);
        unity2 = (Spinner) rootview.findViewById(R.id.unity_2);

        unity1.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity2.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));

        sectionIngredient = rootview.findViewById(R.id.section_ingredients);
        buttonAddIngredient = rootview.findViewById(R.id.addIngredient);
        buttonAddIngredient.setOnClickListener(addIngredient_listener);

        sectionPreparation = rootview.findViewById(R.id.section_preparation);
        buttonAddStep = rootview.findViewById(R.id.addStep);
        buttonAddStep.setOnClickListener(addStep_listener);

        validation = rootview.findViewById(R.id.validate);
        validation.setOnClickListener(validation_listener);

        return rootview;
    }

}
