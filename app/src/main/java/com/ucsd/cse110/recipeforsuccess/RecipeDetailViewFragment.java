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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;



/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailViewFragment extends Fragment {

    private ImageView iv;
    private Bitmap bitmap;

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
    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;

        }
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

                    // getting recipe title and changing font
                    TextView recipeTitleBox = (TextView) v.findViewById(R.id.recipeTitle);
                    Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(),"Pacifico.ttf");
                    recipeTitleBox.setTypeface(typeFace);
                    recipeTitleBox.setText(recipe.get("Name").toString());

                    // getting recipe description and changing font
                    TextView recipeDetailBox = (TextView) v.findViewById(R.id.recipeDetails);
                    Typeface typeFace2 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
                    recipeDetailBox.setTypeface(typeFace2);
                    recipeDetailBox.setText(recipe.get("Description").toString());

                    // getting recipe ingredients and changing font
                    TextView recipeIngredientsBox = (TextView) v.findViewById(R.id.recipeIngredients);
                    Typeface typeFace3 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
                    recipeIngredientsBox.setTypeface(typeFace3);
                    recipeIngredientsBox.setText(recipe.get("Ingredients").toString());

                    // getting recipe instructions and changing font
                    TextView recipeDirectionBox = (TextView) v.findViewById(R.id.recipeInstructions);
                    Typeface typeFace4 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
                    recipeDirectionBox.setTypeface(typeFace4);
                    recipeDirectionBox.setText(recipe.get("Directions").toString());

                    // getting recipe instructions and changing font
                    //TextView recipeImageBox = (TextView) v.findViewById(R.id.recipeDetails);
                    String url = recipe.get("img").toString();
                    iv = (ImageView) v.findViewById(R.id.imageView2);
                    bitmap = getBitmapFromURL(url);
                    iv.setImageBitmap(bitmap);



                }


            }
        });
    }

    void fillInViewContents(View v) {
        //TextView recipeTitleBox = (TextView) this.v.findViewById(R.id.recipeTitle);
        //recipeTitleBox.setText(this.recipeName.toUpperCase());
    }
}
