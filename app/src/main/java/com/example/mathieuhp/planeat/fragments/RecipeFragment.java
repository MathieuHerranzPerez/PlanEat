package com.example.mathieuhp.planeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.mathieuhp.planeat.models.FirebaseDataRetriever;
import com.example.mathieuhp.planeat.models.Recipe;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeFragment extends Fragment implements Updatable, FirebaseDataRetriever {

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
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recipe,null);

        //TODO mettre une barre de chargement

        recipeNameTextView = (TextView) view.findViewById(R.id.DetailedRecipeName);
        recipeNameTextView.setText(recipe.getName());

        if(this.recipe != null && this.recipe.hasComponents()){

            recipeImageView = (ImageView) view.findViewById(R.id.DetailedRecipePicture);
            if(recipe.getImage() != null) {
                recipeImageView.setImageBitmap(recipe.getImage());
            }

            if(recipe.getDescription() != null){
                recipeDescriptionTextView = (TextView) view.findViewById(R.id.DetailedRecipeDescription);
                recipeDescriptionTextView.setText(recipe.getDescription());
            }
        }

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
            recipe.setRecipeDetailedView(this);
            recipe.loadComponents();
        }
    }


    @Override
    public void updateView() {
        if(this.recipe != null && this.recipe.hasComponents()){

            recipeImageView = (ImageView) this.getView().findViewById(R.id.DetailedRecipePicture);
            if(recipe.getImage() != null) {
                recipeImageView.setImageBitmap(recipe.getImage());
            }

            if(recipe.getDescription() != null){
                recipeDescriptionTextView = (TextView) this.getView().findViewById(R.id.DetailedRecipeDescription);
                recipeDescriptionTextView.setText(recipe.getDescription());
            }
        }
    }


    /**** FIREBASE DATA RETRIEVER ****/
    @Override
    public void retrieveData() {
        updateView();
        //refreshing the layout of fragment
        /*
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.detach(this);
        transaction.attach(this);
        transaction.commit();
        */
    }

    @Override
    public void updateData() {

    }
}