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
import android.widget.Button;
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
    private ImageView recipeImageView = null;

    private LinearLayout numberPeopleSection = null;
    private LinearLayout numberPeopleSubSection = null;
    private TextView nbPeopleTextView = null;
    private Button removeNbPeopleButton = null;
    private Button addNbPeopleButton = null;

    private LinearLayout preparationAndDifficultySection = null;
    private LinearLayout preparationTimeSection = null;
    private TextView preparationTimeTextView = null;

    private LinearLayout difficultySection = null;
    private TextView difficultyTextView = null;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recipe,null);

        //TODO mettre une barre de chargement

        if(this.recipe != null && this.recipe.hasComponents()){

            recipeImageView = (ImageView) view.findViewById(R.id.DetailedRecipePicture);
            if(recipe.getImage() != null) {
                recipeImageView.setImageBitmap(recipe.getImage());
            }

            numberPeopleSection = (LinearLayout) view.findViewById(R.id.NumberPeopleSection);
            numberPeopleSubSection = (LinearLayout) numberPeopleSection.findViewById(R.id.NumberPeopleSubSection);

            nbPeopleTextView = (TextView) numberPeopleSubSection.findViewById(R.id.DetailedNbPeopleForRecipe);
            if(recipe.getNbPeople() > 0) {
                nbPeopleTextView.setText(String.valueOf(recipe.getNbPeople()));
            }

            removeNbPeopleButton = (Button) numberPeopleSubSection.findViewById(R.id.RemoveNbPeopleButton);
            removeNbPeopleButton.setOnClickListener(new RemoveNbPeopleListener(this.recipe, this.nbPeopleTextView));

            addNbPeopleButton = (Button) numberPeopleSubSection.findViewById(R.id.AddNbPeopleButton);
            addNbPeopleButton.setOnClickListener(new AddNbPeopleListener(this.recipe, this.nbPeopleTextView));



            preparationAndDifficultySection = (LinearLayout) view.findViewById(R.id.PreparationAndDifficultySection);

            preparationTimeSection = (LinearLayout) preparationAndDifficultySection.findViewById(R.id.PreparationTimeSection);

            preparationTimeTextView = (TextView) preparationTimeSection.findViewById(R.id.RecipePreparationTime);
            if(recipe.getPreparationTime() > 0){
                preparationTimeTextView.setText(recipe.getPreparationTime() + " min");
            }

            difficultySection = (LinearLayout) preparationAndDifficultySection.findViewById(R.id.DifficultySection);

            difficultyTextView = (TextView) difficultySection.findViewById(R.id.RecipeDifficultyLevel);
            if(recipe.getDifficulty() > 0.0){
                difficultyTextView.setText(String.valueOf(recipe.getDifficulty()));
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
    }


    /**** FIREBASE DATA RETRIEVER ****/
    @Override
    public void retrieveData() {
        updateView();
        //refreshing the layout of fragment

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.detach(this);
        transaction.attach(this);
        transaction.commit();

    }

    @Override
    public void updateData() {

    }

    public class RemoveNbPeopleListener implements View.OnClickListener{

        private Recipe recipe;
        private TextView nbPeopleTextView;

        RemoveNbPeopleListener(Recipe recipe, TextView nbPeopleTextView){
            this.recipe = recipe;
            this.nbPeopleTextView = nbPeopleTextView;
        }

        @Override
        public void onClick(View v) {
            this.recipe.decrementNbPeople();
            this.nbPeopleTextView.setText(String.valueOf(this.recipe.getNbPeople()));
            //TODO changer les ingrédients de la recette
        }
    }

    public class AddNbPeopleListener implements View.OnClickListener{

        private Recipe recipe;
        private TextView nbPeopleTextView;

        AddNbPeopleListener(Recipe recipe, TextView nbPeopleTextView){
            this.recipe = recipe;
            this.nbPeopleTextView = nbPeopleTextView;
        }

        @Override
        public void onClick(View v) {
            this.recipe.incrementNbPeople();
            this.nbPeopleTextView.setText(String.valueOf(this.recipe.getNbPeople()));
            //TODO changer les ingrédients de la recette
        }
    }
}