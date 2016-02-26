package com.ucsd.cse110.recipeforsuccess;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Typeface;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailViewFragment extends Fragment {

    String objectID = null;
    String recipeName = null;
    ParseObject parseObject = null;

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

        getRecipeDetails(v);
        fillInViewContents(v);

        return v;
    }

    //this is the function to get all the details to populate this view
    void getRecipeDetails(final View v){

        MainActivity activity = (MainActivity) getActivity();
        this.objectID = activity.getCurSelectedObjectId();
        this.recipeName = activity.getCurSelectedRecipeName();

        ParseQuery<ParseObject> search = ParseQuery.getQuery("Recipe");
        search.whereEqualTo("Name", this.recipeName);
        search.orderByAscending("Name");
        search.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> recipeList, ParseException e) {
                if (e == null) {
                    Log.d("recipe", "Retrieved " + recipeList.size() + "recipes");
                } else {
                    Log.d("recipe", "Error: " + e.getMessage());
                }

                int num_search_results = recipeList.size();
                for (ParseObject recipe : recipeList) {
                    TextView recipeTitleBox = (TextView) v.findViewById(R.id.recipeTitle);
                    Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(),"Pacifico.ttf");
                    recipeTitleBox.setTypeface(typeFace);
                    recipeTitleBox.setText(recipe.get("Name").toString());
                }


            }
        });
    }

    void fillInViewContents(View v) {
        //TextView recipeTitleBox = (TextView) this.v.findViewById(R.id.recipeTitle);
        //recipeTitleBox.setText(this.recipeName.toUpperCase());
    }
}
