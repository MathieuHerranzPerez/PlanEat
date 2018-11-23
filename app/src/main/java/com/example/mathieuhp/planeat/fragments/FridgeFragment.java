package com.example.mathieuhp.planeat.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Fridge;
import com.example.mathieuhp.planeat.models.Ingredient;
import com.example.mathieuhp.planeat.models.User;
import com.example.mathieuhp.planeat.utils.IngredientAdapter;
import com.example.mathieuhp.planeat.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FridgeFragment extends Fragment implements Updatable{

    // give an access for the models to call the update methode
    private static FridgeFragment fridgeFragment;

    private User user;
    private Fridge fridge;

    private JSONArray jsonArray;
    private List<Ingredient> ingredientList;

    private LinearLayout linearLayout;
    private FloatingActionButton buttonAddIngredient;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fridgeFragment = this;
        //get the user
        user = User.getUserInstance();

        ingredientList = new ArrayList<>();
        loadJsonFromAsset();

        fridge = new Fridge();
        user.setFridge(fridge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fridge, null);

        this.linearLayout = (LinearLayout) view.findViewById(R.id.fridge_ingredients);
        this.buttonAddIngredient = (FloatingActionButton) view.findViewById(R.id.btn_add_ingredient);

        // btn to add an ingredient to the ingredient list
        buttonAddIngredient.setOnClickListener(new OnClickListenerAddIngredientBtn(user, fridge));

        updateView();

        return view;
    }

    @Override
    public void updateView() {

        // reset the layout
        linearLayout.removeAllViews();

        // display ingredients by type
        String oldType = "";
        for(Map.Entry<Ingredient, Integer> entry : fridge.getTreeMapIngredient().entrySet()) {
            if(!oldType.equals(entry.getKey().getType())) {
                // display the type
                TextView typeTextView = new TextView(getActivity());
                typeTextView.setText(entry.getKey().getType());
                typeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;
                layoutParams.topMargin = 20;
                typeTextView.setLayoutParams(layoutParams);
                if (typeTextView.getParent() != null)
                    ((ViewGroup) typeTextView.getParent()).removeView(typeTextView);
                linearLayout.addView(typeTextView);
                // line
                View lineView = new View(getActivity());
                lineView.setBackgroundColor(Color.BLACK);
                LinearLayout.LayoutParams layoutParamsLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lineView.setLayoutParams(layoutParamsLine);
                if (lineView.getParent() != null)
                    ((ViewGroup) lineView.getParent()).removeView(lineView);
                linearLayout.addView(lineView);

                oldType = entry.getKey().getType();
            }

            // horizontal linearLayout
            LinearLayout linearLayoutIngredient = new LinearLayout(getActivity());
            LinearLayout.LayoutParams layoutParamsRecipe = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
            linearLayoutIngredient.setLayoutParams(layoutParamsRecipe);
            linearLayoutIngredient.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutIngredient.setPadding(30, 0, 10, 0);

            // name of the ingredient
            TextView textViewIngredientName = new TextView(getActivity());
            LinearLayout.LayoutParams layoutParamsTitle1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 14);
            textViewIngredientName.setLayoutParams(layoutParamsTitle1);
            textViewIngredientName.setGravity(Gravity.CENTER_VERTICAL);
            textViewIngredientName.setText(entry.getKey().getName());

            // editText number of ingredient
            EditText editTextNbIngredient = new EditText(getActivity());
            editTextNbIngredient.setInputType(InputType.TYPE_CLASS_PHONE);
            editTextNbIngredient.setText(String.valueOf(entry.getValue()));
            editTextNbIngredient.setImeOptions(EditorInfo.IME_ACTION_SEND);
            editTextNbIngredient.setOnEditorActionListener(new OnEditorActionListenerQuantity(editTextNbIngredient, entry.getKey(), fridge));

            // less button
            ImageButton imageButtonLess = new ImageButton(getActivity());
            LinearLayout.LayoutParams layoutParamsBtnLess = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            imageButtonLess.setLayoutParams(layoutParamsBtnLess);
            imageButtonLess.setImageResource(R.drawable.ic_action_less);
            imageButtonLess.setOnClickListener(new OnClickListenerAddBtn(editTextNbIngredient, -1, entry.getKey(), fridge));
            imageButtonLess.setBackgroundColor(Color.TRANSPARENT);

            // plus button
            ImageButton imageButtonPlus = new ImageButton(getActivity());
            LinearLayout.LayoutParams layoutParamsBtnPlus = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            imageButtonPlus.setLayoutParams(layoutParamsBtnPlus);
            imageButtonPlus.setImageResource(R.drawable.ic_action_plus);
            imageButtonPlus.setOnClickListener(new OnClickListenerAddBtn(editTextNbIngredient, 1, entry.getKey(), fridge));
            imageButtonPlus.setBackgroundColor(Color.TRANSPARENT);

            // btn delete ingredient
            ImageButton btnDelete = new ImageButton(getActivity());
            LinearLayout.LayoutParams layoutParamsBtnDelete = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            layoutParamsBtnDelete.rightMargin = 25;
            btnDelete.setLayoutParams(layoutParamsBtnDelete);
            btnDelete.setImageResource(R.drawable.ic_action_delete);
            btnDelete.setOnClickListener(new OnClickListenerDeleteIngredient(fridge, entry.getKey()));
            btnDelete.setBackgroundColor(Color.TRANSPARENT);


            linearLayoutIngredient.addView(btnDelete);
            linearLayoutIngredient.addView(textViewIngredientName);
            linearLayoutIngredient.addView(imageButtonLess);
            linearLayoutIngredient.addView(editTextNbIngredient);
            linearLayoutIngredient.addView(imageButtonPlus);

            // add it to the main layout
            linearLayout.addView(linearLayoutIngredient);
        }
    }

    /**
     * Instantiate the ingredients by checking the JSON file
     */
    private void loadJsonFromAsset() {

        try {
            InputStream is = getActivity().getAssets().open("ingredientsCalories.json");

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Ingredient ingr = new Ingredient(obj.getString("id"), obj.getString("name"),
                         obj.getString("kiloCaloriesPerHundredGrams"), obj.getString("type"));
            }

        }catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* ---- LISTENERS ---- */
    private class OnClickListenerAddBtn implements View.OnClickListener {

        private EditText editText;
        private int nb;
        private Fridge fridge;
        private Ingredient ingredient;

        private OnClickListenerAddBtn(EditText editText, int nb, Ingredient ingredient, Fridge fridge) {
            this.editText = editText;
            this.nb = nb;
            this.ingredient = ingredient;
            this.fridge = fridge;
        }

        @Override
        public void onClick(View view) {
            if(Utils.isInteger(editText.getText().toString())) {
                int nbIngr = Integer.parseInt(editText.getText().toString());
                if ((nbIngr + nb) >= 0) {
                    nbIngr += nb;
                    String res = Integer.toString(nbIngr);
                    editText.setText(res);

                    fridge.updateQuantity(ingredient, res);
                    editText.setText(res);
                }
            }
        }
    }

    private class OnEditorActionListenerQuantity implements TextView.OnEditorActionListener {

        private EditText editText;

        private Fridge fridge;
        private Ingredient ingredient;

        public OnEditorActionListenerQuantity(EditText editText, Ingredient ingredient, Fridge fridge) {
            this.editText = editText;
            this.fridge = fridge;
            this.ingredient = ingredient;
        }

        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEND) {
                if(Utils.isInteger(editText.getText().toString())) {

                    int nbIngr = Integer.parseInt(editText.getText().toString());

                    if(nbIngr >= 0) {
                        String res = Integer.toString(nbIngr);
                        Log.d("RES", res);
                        fridge.updateQuantity(ingredient, res);
                    }
                }
                return true;
            }
            return false;
        }
    }

    private class OnClickListenerAddIngredientBtn implements View.OnClickListener {

        private Fridge fridge;
        private User user;

        private OnClickListenerAddIngredientBtn(User user, Fridge fridge) {
            this.fridge = fridge;
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            DialogAddIngredient addIngedientWindow = new DialogAddIngredient(getActivity(), fridge);
            addIngedientWindow.show();
        }

        /**
         * Window where the user can choose an ingredient
         */
        private class DialogAddIngredient extends Dialog implements android.view.View.OnClickListener {

            private Fridge fridge;
            private ArrayList<Ingredient> temporaryList;

            private Activity activity;
            private Button btnCancel;
            private SearchView searchView;
            private ListView ingredientListView;

            private IngredientAdapter adapter;

            private DialogAddIngredient(Activity a, Fridge fridge) {
                super(a);
                this.activity = a;
                this.fridge = fridge;

                temporaryList = new ArrayList<>();
                temporaryList.addAll(Ingredient.ingredientList);
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.dialog_add_ingredient_fridge);

                ingredientListView = (ListView) findViewById(R.id.fridge_ingredient_list);
                searchView = (SearchView) findViewById(R.id.search_ingredient_filter);
                btnCancel = (Button) findViewById(R.id.btn_cancel);

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
                        fridge.addIngedient(ingredient);
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
    }

    private class OnClickListenerDeleteIngredient implements View.OnClickListener {

        private Fridge fridge;
        private Ingredient ingredient;

        public OnClickListenerDeleteIngredient(Fridge fridge, Ingredient ingredient) {
            this.fridge = fridge;
            this.ingredient = ingredient;
        }

        @Override
        public void onClick(View view) {
            fridge.deleteIngredient(ingredient);
        }
    }


    /**
     * give an access for the models to call the update methode
     * @return FridgeFragment, the last instance of FridgeFragment
     */
    public static FridgeFragment getFridgeFragment() {
        return fridgeFragment;
    }
}
