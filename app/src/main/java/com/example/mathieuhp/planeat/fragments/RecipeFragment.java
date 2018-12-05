package com.example.mathieuhp.planeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.RecipesListItemDecoration;
import com.example.mathieuhp.planeat.adapters.RecipesListItemAdapter;
import com.example.mathieuhp.planeat.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeFragment extends Fragment implements Updatable {

    private Context context = getActivity();

    //model
    private Recipe recipe;

    //elements of UI
    private TextView recipeNameTextView = null;
    private ImageView recipeImageView = null;
    private TextView recipeDescriptionTextView = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, null);

        //setting list of recipes
        recipeNameTextView = (TextView) view.findViewById(R.id.RecipeName);
        recipeNameTextView.setText(recipe.getName());

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // allow toolbar
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // get the user from the activity
            recipe = bundle.getParcelable("recipe");
        }
    }


    @Override
    public void updateView() {
        // TODO
    }


}