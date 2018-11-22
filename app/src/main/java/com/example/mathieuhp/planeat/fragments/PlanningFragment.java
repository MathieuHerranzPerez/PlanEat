package com.example.mathieuhp.planeat.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Recipe;
import com.example.mathieuhp.planeat.models.RecipeCalendar;
import com.example.mathieuhp.planeat.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanningFragment extends Fragment implements Updatable{

    // give an access for the models to call the update methode
    private static PlanningFragment planningFragment;

    private User user;
    private RecipeCalendar recipeCalendar;

    private LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planningFragment = this;
        // get the user
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // get the user from the activity
            user = bundle.getParcelable("user");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning, null);

        this.linearLayout = (LinearLayout) view.findViewById(R.id.calendar_recipes_list);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        this.recipeCalendar = user.getRecipeCalendar();

        updateView();
    }

    @Override
    public void updateView() {
        // reset the layout
        linearLayout.removeAllViews();

        for(Map.Entry<Integer, Pair<Recipe, Recipe> > entry : recipeCalendar.getRecipesListPerDay().entrySet()) {
            // display the day
            TextView dayTextView = new TextView(getActivity());
            dayTextView.setText(getResources().getString(getResources().getIdentifier("day_" + Integer.toString(entry.getKey() % 10), "string", getActivity().getPackageName())));
            dayTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.topMargin = 20;
            dayTextView.setLayoutParams(layoutParams);
            if (dayTextView.getParent() != null)
                ((ViewGroup) dayTextView.getParent()).removeView(dayTextView);
            linearLayout.addView(dayTextView);
            // line
            View lineView = new View(getActivity());
            lineView.setBackgroundColor(Color.BLACK);
            LinearLayout.LayoutParams layoutParamsLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lineView.setLayoutParams(layoutParamsLine);
            if (lineView.getParent() != null)
                ((ViewGroup) lineView.getParent()).removeView(lineView);
            linearLayout.addView(lineView);

            // RECIPE
            // iterate on the pair
            for(int i = 1; i < 3; ++i) {
                // horizontal linearLayout
                LinearLayout linearLayoutRecipe = new LinearLayout(getActivity());
                LinearLayout.LayoutParams layoutParamsRecipe = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
                linearLayoutRecipe.setLayoutParams(layoutParamsRecipe);
                linearLayoutRecipe.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutRecipe.setPadding(30, 0, 10, 0);
                linearLayoutRecipe.setFocusable(true);
                linearLayoutRecipe.setClickable(true);

                // get the recipe name and calories from the user
                Pair<String, String> resNameCaloriesRecipe;
                //String idRecipeValue;
                // if there is a recipe in the pair,
                if(i == 1 && entry.getValue().first != null) {
                    //idRecipeValue = entry.getValue().first.getId();
                    resNameCaloriesRecipe = new Pair<>(entry.getValue().first.getName(), Integer.toString(entry.getValue().first.getCalories()));
                    linearLayoutRecipe.setOnClickListener(new OnClickListenerBtnRecipe(entry.getValue().first));
                    //resNameCaloriesRecipe = new Pair<>(user.getListPersonnalAndFollowedRecipe().get(idRecipeValue).getName(), Integer.toString(user.getListPersonnalAndFollowedRecipe().get(idRecipeValue).getCalories()));
                }
                else if(i != 1 && entry.getValue().second != null) {
                    //idRecipeValue = entry.getValue().second.getId();
                    resNameCaloriesRecipe = new Pair<>(entry.getValue().second.getName(), Integer.toString(entry.getValue().second.getCalories()));
                    linearLayoutRecipe.setOnClickListener(new OnClickListenerBtnRecipe(entry.getValue().second));
                    //resNameCaloriesRecipe = new Pair<>(user.getListPersonnalAndFollowedRecipe().get(idRecipeValue).getName(), Integer.toString(user.getListPersonnalAndFollowedRecipe().get(idRecipeValue).getCalories()));
                }
                else {
                    resNameCaloriesRecipe = new Pair<>(getResources().getString(R.string.empty_recipe), "0");
                }

                // title of the recipe
                TextView titleRecipeTextView = new TextView(getActivity());
                LinearLayout.LayoutParams layoutParamsTitle1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 6);
                titleRecipeTextView.setLayoutParams(layoutParamsTitle1);
                titleRecipeTextView.setGravity(Gravity.CENTER_VERTICAL);
                titleRecipeTextView.setText(resNameCaloriesRecipe.first);

                // calories of the recipe
                TextView caloriesRecipeTextView = new TextView(getActivity());
                LinearLayout.LayoutParams layoutParamsCalories = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
                caloriesRecipeTextView.setLayoutParams(layoutParamsCalories);
                caloriesRecipeTextView.setGravity(Gravity.CENTER_VERTICAL);
                String cal = resNameCaloriesRecipe.second + " kcal";
                caloriesRecipeTextView.setText(cal);

                //button change
                ImageButton btnChangeRecipe = new ImageButton(getActivity());
                LinearLayout.LayoutParams layoutParamsBtnChange = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
                btnChangeRecipe.setLayoutParams(layoutParamsBtnChange);
                btnChangeRecipe.setImageResource(R.drawable.ic_action_change_edit);
                btnChangeRecipe.setOnClickListener(new OnClickListenerChangeRecipe(entry.getKey() + i/10f));

                linearLayoutRecipe.addView(titleRecipeTextView);
                linearLayoutRecipe.addView(caloriesRecipeTextView);
                linearLayoutRecipe.addView(btnChangeRecipe);

                // add it to the main layout
                linearLayout.addView(linearLayoutRecipe);
            }
        }
    }

    private class OnClickListenerBtnRecipe implements View.OnClickListener {

        private Recipe recipe;

        private OnClickListenerBtnRecipe(Recipe r) {
            this.recipe = r;
        }

        @Override
        public void onClick(View view) {
            // TODO start the recipe fragment
            Log.d("Click recipe", "OUIIII");        // affD
        }
    }

    private class OnClickListenerChangeRecipe implements View.OnClickListener {

        private float date;

        private OnClickListenerChangeRecipe(Float date) {
            this.date = date;
        }

        @Override
        public void onClick(View view) {
            DialogChangeCalendarRecipe changeCalendarRecipeWindow = new DialogChangeCalendarRecipe(getActivity(), user, date);
            changeCalendarRecipeWindow.show();
        }


        private class DialogChangeCalendarRecipe extends Dialog implements android.view.View.OnClickListener {

            private Activity activity;
            private Button btnChange;
            private Button btnCancel;
            private Spinner spinnerRecipe;

            private User user;
            private Float date;

            private DialogChangeCalendarRecipe(Activity a, User user, float date) {
                super(a);
                this.activity = a;
                this.user = user;
                this.date = date;
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.dialog_change_calendar_recipe);

                btnChange = (Button) findViewById(R.id.btn_change);
                btnCancel = (Button) findViewById(R.id.btn_cancel);
                spinnerRecipe = (Spinner) findViewById(R.id.spinner_recipes);

                // get all the user recipes (followed and owned)
                List<Recipe> recipeList = new ArrayList<>();
                recipeList = user.getAllRecipes();

                // create an adapter for the spinner
                ArrayAdapter<Recipe> adapterRecipe = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, recipeList);
                adapterRecipe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerRecipe.setAdapter(adapterRecipe);

                btnChange.setOnClickListener(this);
                btnCancel.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_change:
                        Recipe r = (Recipe) spinnerRecipe.getSelectedItem();
                        recipeCalendar.saveRecipeForDate(r, date);
                        break;
                    case R.id.btn_cancel:
                        dismiss();
                        break;
                    default:
                        break;
                }
                dismiss();
            }
        }
    }

    /**
     * give an access for the models to call the update methode
     * @return PlanningFragment, the last instance of PlanningFragment
     */
    public static PlanningFragment getPlanningFragment() {
        return planningFragment;
    }
}
