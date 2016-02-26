package com.ucsd.cse110.recipeforsuccess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailViewFragment extends Fragment {

    String objectID = null;
    String recipeName = null;

    public RecipeDetailViewFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailViewFragment newInstance(String name) {
        RecipeDetailViewFragment fragment = new RecipeDetailViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipe_detail_view, container, false);

        getRecipeDetails();
        fillInViewContents(v);

        return v;
    }

    //this is the function to get all the details to populate this view
    void getRecipeDetails(){

        MainActivity activity = (MainActivity) getActivity();
        this.objectID = activity.getCurSelectedObjectId();
        this.recipeName = activity.getCurSelectedRecipeName();
    }

    void fillInViewContents(View v){

        TextView recipeTitleBox = (TextView) v.findViewById(R.id.recipeTitle);
        recipeTitleBox.setText(this.recipeName.toUpperCase());

    }
}
