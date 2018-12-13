package com.example.mathieuhp.planeat.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Component;
import com.example.mathieuhp.planeat.models.Ingredient;
import com.example.mathieuhp.planeat.models.QuantityUnit;
import com.example.mathieuhp.planeat.models.Recipe;
import com.example.mathieuhp.planeat.models.Tags;
import com.example.mathieuhp.planeat.utils.IngredientAdapter;
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

    //section to add tags
    private GridLayout sectionTag;
    private TextView newTag;
    private FloatingActionButton buttonAddTag;
    private View.OnClickListener addTag_listener;
    private ArrayList<String> items;

    //section to add ingredient
    private LinearLayout sectionIngredient;
    //set a new ingredient
    private Spinner unity1;
    private Button buttonSetIngredient1;
    private View.OnClickListener setIngredient1_listener;
    private Spinner unity2;
    private Button buttonSetIngredient2;
    private View.OnClickListener setIngredient2_listener;
    //Add a new ingredient
    private FloatingActionButton buttonAddIngredient;
    private View.OnClickListener addIngredient_listener;
    private int nbIngredient = 2;
    private Button buttnSetIngredient;
    private View.OnClickListener setIngredient_listener;

    //section to add a step
    private LinearLayout sectionPreparation;
    private FloatingActionButton buttonAddStep;
    private View.OnClickListener addStep_listener;
    private int nbStep = 1;

    //section to send the recipe
    private FloatingActionButton validation;
    private View.OnClickListener validation_listener;

    private boolean isPut = false;
    private boolean isDifficultyValid;
    private boolean isComplete;

    private Recipe newRecipe;

    private Component newComponent1;
    private Component newComponent2;
    private Component component;

    private ArrayList<String> tag;
    private Ingredient newIngredient;
    private ArrayList<Component> ingredients;

    private ArrayList<String> preparation;
    private int nbRecipe = 0;

    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();

    //button to add tags
    {
        addTag_listener = new Button.OnClickListener() {
            @Override
            public  void onClick (View arg0) {
                DialogAddTag addTag = new DialogAddTag(getActivity());
                addTag.show();
            }
        };
    }

    //button to add the ingredient 1 in the recipe
    {
        setIngredient1_listener = new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogAddIngredient addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent1, buttonSetIngredient1);
                addIngredientWindow.show();
                //buttonSetIngredient1.setText(newComponent1.getIngredient().getName());
            }
        };
    }

    //button to add the ingredient 2 in the recipe
    {
        setIngredient2_listener = new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogAddIngredient addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent2, buttonSetIngredient2);
                addIngredientWindow.show();
                //buttonSetIngredient1.setText(newComponent2.getIngredient().getName());
            }
        };
    }

    //button to add the others ingredients in the recipe
    {
        setIngredient_listener = new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogAddIngredient addIngredientWindow = new DialogAddIngredient(getActivity(), component, buttnSetIngredient);
                addIngredientWindow.show();
                //buttonSetIngredient1.setText(component.getIngredient().getName());
            }
        };
    }

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

                DialogAddIngredient addIngredientWindow = new DialogAddIngredient(getActivity(), component, buttnSetIngredient);
                addIngredientWindow.show();

                //Drop-down
                Spinner dropdown_unity = new Spinner(myContext);
                addIngredient.addView(dropdown_unity);
                dropdown_unity.setTag("dropdown_unity_" + nbIngredient);
                dropdown_unity.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
                LinearLayout.LayoutParams paramUnity = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dropdown_unity.setLayoutParams(paramUnity);



                //Textview for ingredient
                buttnSetIngredient = new Button(myContext);
                addIngredient.addView(buttnSetIngredient);
                buttnSetIngredient.setTag("set_ingredient_" + nbIngredient);
                buttnSetIngredient.setHint(R.string.hint_ingredient);
                LinearLayout.LayoutParams paramIngredient = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4f);
                buttnSetIngredient.setLayoutParams(paramIngredient);
                buttnSetIngredient.setOnClickListener(setIngredient_listener);

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

    //button to create the recipe
    {
        validation_listener = new ImageButton.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                isDifficultyValid = true;
                isComplete = true;

                //Set the recipe name and it description
                EditText edit_name = rootview.findViewById(R.id.name);
                if (edit_name.getText().length() == 0) {
                    isComplete = false;
                }
                else newRecipe.setName(edit_name.getText().toString());

                EditText edit_desc = rootview.findViewById(R.id.description);
                if (edit_desc.getText().length() == 0) {
                    isComplete = false;
                }
                else newRecipe.setDescription(edit_desc.getText().toString());


                //Set the picture
                //TODO Load picture

                //TODO beta : calculate calories for 1 person
                //calories = 12;
                // somme des calorie de chaques ingredients / le nombre de personnes

                //Set the tags
                newRecipe.setTags(items);

                //set the nb of people, the time and the difficulty
                EditText edit_nbPerson = rootview.findViewById(R.id.edit_nbPerson);
                if (edit_nbPerson.getText().length() == 0) {
                    isComplete = false;
                }
                else newRecipe.setNbPeople(Integer.parseInt(edit_nbPerson.getText().toString()));

                EditText edit_prepTime = rootview.findViewById(R.id.edit_preparationTime);
                if (edit_prepTime.getText().length() == 0) {
                    isComplete = false;
                }
                else newRecipe.setPreparationTime(Integer.parseInt(edit_prepTime.getText().toString()));

                EditText edit_difficulty = rootview.findViewById(R.id.edit_difficulty);
                if (edit_difficulty.getText().length() == 0) {
                    isComplete = false;
                }
                else {
                    int dif = Integer.parseInt(edit_difficulty.getText().toString());
                    if (dif >= 0 && dif <= 5) {
                        newRecipe.setDifficulty(dif);
                    }
                    else isDifficultyValid = false;
                }

                //set the ingredients
                EditText edit_qtyIngredient1 = rootview.findViewById(R.id.edit_quantity_1);
                Button set_ingredient1 = rootview.findViewById(R.id.set_ingredient_l);

                EditText edit_qtyIngredient2 = rootview.findViewById(R.id.edit_quantity_2);
                Button set_ingredient2 = rootview.findViewById(R.id.set_ingredient_2);

                if (edit_qtyIngredient1.getText().length() == 0 || edit_qtyIngredient2.getText().length() == 0 ||
                        set_ingredient1.getText().length() == 0 || set_ingredient2.getText().length() == 0) {
                    isComplete = false;
                } else {
                    //first and second ingredients

                    newComponent1.setQuantity(Float.parseFloat(edit_qtyIngredient1.getText().toString()));
                    newComponent1.setUnity(QuantityUnit.valueOf(unity1.getSelectedItem().toString()));
                    
                    //ingredient1.add(set_ingredient1.getText().toString());

                    newComponent2.setQuantity(Float.parseFloat(edit_qtyIngredient2.getText().toString()));
                    newComponent2.setUnity(QuantityUnit.valueOf(unity2.getSelectedItem().toString()));

                    //ingredient2.add(set_ingredient2.getText().toString());

                    ingredients.add(newComponent1);
                    ingredients.add(newComponent2);

                    //other ingredients
                    for (int i = 3; i <= nbIngredient; i++) {

                        EditText edit_qtyIngredient = rootview.findViewWithTag("edit_quantity_" + i);
                        String string_edit_quantity = edit_qtyIngredient.getText().toString().trim();
                        Spinner unity = rootview.findViewWithTag("dropdown_unity_" + i);
                        EditText edit_ingredient = rootview.findViewWithTag("edit_ingredient_" + i);
                        String string_edit_ingredient = edit_ingredient.getText().toString().trim();

                        if (nbIngredient > 2 && (!string_edit_quantity.isEmpty() || !string_edit_ingredient.isEmpty())) {

                            component.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                            component.setUnity(QuantityUnit.valueOf(unity.getSelectedItem().toString()));

                            //ingredient.add(edit_ingredient.getText().toString());
                            ingredients.add(component);

                        }
                    }
                }

                EditText edit_preparation = rootview.findViewById(R.id.edit_step1);
                if (edit_preparation.getText().length() == 0) {
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
                    newRecipe.setPreparation(preparation);
                }

                //Recipe not shared yet
                newRecipe.setShared(false);

                //Verifying if the recipe is complete
                if (!isDifficultyValid) {
                    Snackbar snackbarDifficulty = Snackbar.make(rootview, R.string.invalid_difficulty, Snackbar.LENGTH_SHORT);
                    snackbarDifficulty.show();
                } else if (!isComplete) {
                    Snackbar snackbarComplete = Snackbar.make(rootview, R.string.invalid_completion, Snackbar.LENGTH_SHORT);
                    snackbarComplete.show();
                } else if (isPut) {
                    Snackbar snackbarExisting = Snackbar.make(rootview, R.string.existing_recipe, Snackbar.LENGTH_SHORT);
                    snackbarExisting.show();
                } else {

                    //put the recipe in Firebase

                    /*
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
                    */

                    Snackbar snackbarComplete = Snackbar.make(rootview, "Recette envoyée", Snackbar.LENGTH_SHORT);
                    snackbarComplete.show();

                    isPut = true;

                }


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
        ingredients = new ArrayList<>();
        newComponent1 = new Component();
        newComponent2 = new Component();
        component = new Component();
        newIngredient = new Ingredient();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_new_recipe, null);


        //to know the id of the recipe
        firebaseReference.child("recipes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                nbRecipe = 0;
                nbRecipe = count.incrementAndGet() + 1;
                newRecipe.setId(String.valueOf(nbRecipe));
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

        sectionTag = rootview.findViewById(R.id.tags);

        buttonAddTag = rootview.findViewById(R.id.addTag);
        buttonAddTag.setOnClickListener(addTag_listener);

        sectionIngredient = rootview.findViewById(R.id.section_ingredients);

        unity1 = rootview.findViewById(R.id.unity_1);
        unity1.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        buttonSetIngredient1 = rootview.findViewById(R.id.set_ingredient_l);
        buttonSetIngredient1.setOnClickListener(setIngredient1_listener);

        unity2 = rootview.findViewById(R.id.unity_2);
        unity2.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        buttonSetIngredient2 = rootview.findViewById(R.id.set_ingredient_2);
        buttonSetIngredient2.setOnClickListener(setIngredient2_listener);

        buttonAddIngredient = rootview.findViewById(R.id.addIngredient);
        buttonAddIngredient.setOnClickListener(addIngredient_listener);

        sectionPreparation = rootview.findViewById(R.id.section_preparation);
        buttonAddStep = rootview.findViewById(R.id.addStep);
        buttonAddStep.setOnClickListener(addStep_listener);

        validation = rootview.findViewById(R.id.validate);
        validation.setOnClickListener(validation_listener);

        return rootview;
    }


    /**
     * Window where the user can choose an ingredient
     */
    final class DialogAddIngredient extends Dialog implements android.view.View.OnClickListener {

        private ArrayList<Ingredient> temporaryList;

        private Activity activity;
        private Button btnCancel;
        private SearchView searchView;
        private ListView ingredientListView;
        private Component component;
        private Button button;

        private IngredientAdapter adapter;

        private DialogAddIngredient(Activity a, Component component, Button button) {
            super(a);
            this.activity = a;
            this.component = component;
            this.button = button;

            temporaryList = new ArrayList<>();
            temporaryList.addAll(Ingredient.ingredientList);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_add_ingredient);

            ingredientListView = findViewById(R.id.ingredient_list);
            searchView = findViewById(R.id.search_ingredient_filter);
            btnCancel = findViewById(R.id.btn_cancel);

            // adapter
            adapter = new IngredientAdapter(activity, R.layout.row_ingredient, temporaryList);
            ingredientListView.setAdapter(adapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    adapter.getFilter().filter(s);
                    return false;
                }
            });

            // by clicking on an Ingredient, add it to the ingredient user list
            ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Ingredient ingredient = (Ingredient) adapterView.getAdapter().getItem(i);
                    component.setIngredient(ingredient);
                    button.setText(ingredient.getName());
                    dismiss();
                }
            });

            btnCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            dismiss();
        }
    }

    /**
     * Window where the user can choose the tags
     */
    final class DialogAddTag extends Dialog implements android.view.View.OnClickListener {


        private Activity activity;
        private Button btnValid;
        private View.OnClickListener buttonValid_listener;
        private Button btnCancel;
        private ListView tagListView;


        private DialogAddTag(Activity a) {
            super(a);
            this.activity = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_add_tag);

            tagListView = findViewById(R.id.tag_list);
            btnValid = findViewById(R.id.btn_addTag);
            btnValid.setOnClickListener(buttonValid_listener);
            btnCancel = findViewById(R.id.btn_cancel);

            tagListView.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_list_item_checked, Tags.values()));

            btnCancel.setOnClickListener(this);

            items = new ArrayList<>();

        }

        {
            buttonValid_listener = new Button.OnClickListener() {
            @Override
            public  void onClick (View arg0) {
                sectionTag.removeAllViews();
                for (int i = tagListView.getFirstVisiblePosition(); i< tagListView.getFirstVisiblePosition() + tagListView.getChildCount();i++) {
                    View nextChild = tagListView.getChildAt(i);
                    if (nextChild instanceof CheckedTextView ) {
                        CheckedTextView  check = (CheckedTextView ) nextChild;
                        if (check.isChecked()) {
                            newTag = new TextView(myContext);
                            newTag.setText(check.getText().toString());
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            param.setMargins(10,10,10,10);
                            newTag.setPadding(10,10,10,10);
                            newTag.setLayoutParams(param);
                            newTag.setBackgroundResource(R.color.colorBackgroundMenu);
                            newTag.setGravity(Gravity.CENTER);
                            sectionTag.addView(newTag);
                            items.add(check.getText().toString());
                        }
                    }
                 }
                if (items.size() > 3 ) {
                    int numberRow = items.size()/3;
                    sectionTag.setRowCount(numberRow);
                }
                dismiss();
                }
            };
        }

        @Override
        public void onClick(View view) {
            dismiss();
        }
    }
}
