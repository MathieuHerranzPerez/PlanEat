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
import android.widget.Toast;

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
    private View.OnClickListener setIngredient_listener;
    private Spinner unity2;
    private Button buttonSetIngredient2;
    private Spinner unity3;
    private Button buttonSetIngredient3;
    private Spinner unity4;
    private Button buttonSetIngredient4;
    private Spinner unity5;
    private Button buttonSetIngredient5;
    private Spinner unity6;
    private Button buttonSetIngredient6;
    private Spinner unity7;
    private Button buttonSetIngredient7;
    private Spinner unity8;
    private Button buttonSetIngredient8;
    private Spinner unity9;
    private Button buttonSetIngredient9;
    private Spinner unity10;
    private Button buttonSetIngredient10;

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
    private Component newComponent3;
    private Component newComponent4;
    private Component newComponent5;
    private Component newComponent6;
    private Component newComponent7;
    private Component newComponent8;
    private Component newComponent9;
    private Component newComponent10;

    private ArrayList<String> tag;
    private Ingredient newIngredient;
    private ArrayList<Component> ingredients;
    private float calories;

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

    //buttons to add an ingredient in the recipe
    {
        setIngredient_listener = new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DialogAddIngredient addIngredientWindow;
                switch (arg0.getId()) {
                    case R.id.set_ingredient_l:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent1, buttonSetIngredient1,1);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_2:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent2, buttonSetIngredient2,2);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_3:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent3, buttonSetIngredient3,3);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_4:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent4, buttonSetIngredient4,4);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_5:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent5, buttonSetIngredient5,5);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_6:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent6, buttonSetIngredient6,6);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_7:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent7, buttonSetIngredient7,7);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_8:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent8, buttonSetIngredient8,8);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_9:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent9, buttonSetIngredient9,9);
                        addIngredientWindow.show();
                        break;
                    case R.id.set_ingredient_10:
                        addIngredientWindow = new DialogAddIngredient(getActivity(), newComponent10, buttonSetIngredient10,10);
                        addIngredientWindow.show();
                        break;
                }
            }
        };
    }

    //button to add dynamically a new ingredient in the recipe
    /*{
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

//                DialogAddIngredient addIngredientWindow = new DialogAddIngredient(getActivity(), component, buttnSetIngredient);
//                addIngredientWindow.show();

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
    }*/

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
                    ingredients.add(newComponent1);
                    calories = newRecipe.calculCalorie(newComponent1);

                    newComponent2.setQuantity(Float.parseFloat(edit_qtyIngredient2.getText().toString()));
                    newComponent2.setUnity(QuantityUnit.valueOf(unity2.getSelectedItem().toString()));
                    ingredients.add(newComponent2);
                    calories += newRecipe.calculCalorie(newComponent2);

                    //other ingredients
                    for (int i = 3; i <= 10; i++) {

                        EditText edit_qtyIngredient = rootview.findViewWithTag("edit_quantity_" + i);
                        String string_edit_quantity = edit_qtyIngredient.getText().toString().trim();
                        Spinner unity = rootview.findViewWithTag("dropdown_unity_" + i);

                        if (!string_edit_quantity.isEmpty()) {
                            switch (i){
                                case 3:
                                    newComponent3.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent3.setUnity(QuantityUnit.valueOf(unity3.getSelectedItem().toString()));
                                    ingredients.add(newComponent3);
                                    calories += newRecipe.calculCalorie(newComponent3);
                                case 4:
                                    newComponent4.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent4.setUnity(QuantityUnit.valueOf(unity4.getSelectedItem().toString()));
                                    ingredients.add(newComponent4);
                                    calories += newRecipe.calculCalorie(newComponent4);
                                case 5:
                                    newComponent5.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent5.setUnity(QuantityUnit.valueOf(unity5.getSelectedItem().toString()));
                                    ingredients.add(newComponent5);
                                    calories += newRecipe.calculCalorie(newComponent5);
                                case 6:
                                    newComponent6.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent6.setUnity(QuantityUnit.valueOf(unity6.getSelectedItem().toString()));
                                    ingredients.add(newComponent6);
                                    calories += newRecipe.calculCalorie(newComponent6);
                                case 7:
                                    newComponent7.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent7.setUnity(QuantityUnit.valueOf(unity7.getSelectedItem().toString()));
                                    ingredients.add(newComponent7);
                                    calories += newRecipe.calculCalorie(newComponent7);
                                case 8:
                                    newComponent8.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent8.setUnity(QuantityUnit.valueOf(unity8.getSelectedItem().toString()));
                                    ingredients.add(newComponent8);
                                    calories += newRecipe.calculCalorie(newComponent8);
                                case 9:
                                    newComponent9.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent9.setUnity(QuantityUnit.valueOf(unity9.getSelectedItem().toString()));
                                    ingredients.add(newComponent9);
                                    calories += newRecipe.calculCalorie(newComponent9);
                                case 10:
                                    newComponent10.setQuantity(Float.parseFloat(edit_qtyIngredient.getText().toString()));
                                    newComponent10.setUnity(QuantityUnit.valueOf(unity10.getSelectedItem().toString()));
                                    ingredients.add(newComponent10);
                                    calories += newRecipe.calculCalorie(newComponent10);
                            }

                        }
                    }
                }

                newRecipe.setCalories((int) calories);

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
                    newRecipe.addRecipeToDatabase(newRecipe);

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
        newComponent3 = new Component();
        newComponent4 = new Component();
        newComponent5 = new Component();
        newComponent6 = new Component();
        newComponent7 = new Component();
        newComponent8 = new Component();
        newComponent9 = new Component();
        newComponent10 = new Component();
        newIngredient = new Ingredient();
        calories = 0;
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
        unity2 = rootview.findViewById(R.id.unity_2);
        unity2.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity3 = rootview.findViewById(R.id.unity_3);
        unity3.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity4 = rootview.findViewById(R.id.unity_4);
        unity4.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity5 = rootview.findViewById(R.id.unity_5);
        unity5.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity6 = rootview.findViewById(R.id.unity_6);
        unity6.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity7 = rootview.findViewById(R.id.unity_7);
        unity7.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity8 = rootview.findViewById(R.id.unity_8);
        unity8.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity9 = rootview.findViewById(R.id.unity_9);
        unity9.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));
        unity10 = rootview.findViewById(R.id.unity_10);
        unity10.setAdapter(new ArrayAdapter<>(myContext, android.R.layout.simple_spinner_dropdown_item, QuantityUnit.values()));

        buttonSetIngredient1 = rootview.findViewById(R.id.set_ingredient_l);
        buttonSetIngredient2 = rootview.findViewById(R.id.set_ingredient_2);
        buttonSetIngredient3 = rootview.findViewById(R.id.set_ingredient_3);
        buttonSetIngredient4 = rootview.findViewById(R.id.set_ingredient_4);
        buttonSetIngredient5 = rootview.findViewById(R.id.set_ingredient_5);
        buttonSetIngredient6 = rootview.findViewById(R.id.set_ingredient_6);
        buttonSetIngredient7 = rootview.findViewById(R.id.set_ingredient_7);
        buttonSetIngredient8 = rootview.findViewById(R.id.set_ingredient_8);
        buttonSetIngredient9 = rootview.findViewById(R.id.set_ingredient_9);
        buttonSetIngredient10 = rootview.findViewById(R.id.set_ingredient_10);

        buttonSetIngredient1.setOnClickListener(setIngredient_listener);
        buttonSetIngredient2.setOnClickListener(setIngredient_listener);
        buttonSetIngredient3.setOnClickListener(setIngredient_listener);
        buttonSetIngredient4.setOnClickListener(setIngredient_listener);
        buttonSetIngredient5.setOnClickListener(setIngredient_listener);
        buttonSetIngredient6.setOnClickListener(setIngredient_listener);
        buttonSetIngredient7.setOnClickListener(setIngredient_listener);
        buttonSetIngredient8.setOnClickListener(setIngredient_listener);
        buttonSetIngredient9.setOnClickListener(setIngredient_listener);
        buttonSetIngredient10.setOnClickListener(setIngredient_listener);

//        buttonAddIngredient = rootview.findViewById(R.id.addIngredient);
//        buttonAddIngredient.setOnClickListener(addIngredient_listener);

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
        private int nbIng;

        private IngredientAdapter adapter;

        private DialogAddIngredient(Activity a, Component component, Button button, int nbIng) {
            super(a);
            this.activity = a;
            this.component = component;
            this.button = button;
            this.nbIng = nbIng;

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
