package com.ucsd.cse110.recipeforsuccess;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RatingBar;
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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.ImageView;



import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailViewFragment extends Fragment {

    private ImageView iv;  //variable for image
    float oldRating;
    float userRating;
    float ratingSum;
    float numberTimesRated;
    RatingBar recipeRateBar;
    TextView rateText;
    Button buttonSubmit;
    //private ParseObject recipe;

    String objectID = null;
    String recipeName = null;
    ParseObject parseObject;


    public RecipeDetailViewFragment() {
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

        // Required empty public constructor
        getActivity().getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        RatingBarClass rateBarOn = new RatingBarClass();
        rateBarOn.listenerForRatingBar(v);
        rateBarOn.onButtonClick(v);
        getRecipeDetails(v);
        fillInViewContents(v);


        return v;
    }

    public class RatingBarClass extends AppCompatActivity {

        public void listenerForRatingBar(View v) {
            recipeRateBar = (RatingBar) v.findViewById(R.id.ratingBar);
            rateText = (TextView) v.findViewById(R.id.textView5);

            recipeRateBar.setOnRatingBarChangeListener(
                    new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar recipeRatingBar, float rating, boolean clicked) {
                            //rateText.setText(String.valueOf(rating));
                            userRating = rating;
                        }
                    }
            );

        }


        //@Override
        public void onButtonClick(View v) {
            recipeRateBar = (RatingBar) v.findViewById(R.id.ratingBar);
            buttonSubmit = (Button) v.findViewById(R.id.button2);

            buttonSubmit.setOnClickListener(
                new View.OnClickListener() {
                    //@Override
                    public void onClick(View v) {
                        float newRating = ((ratingSum+userRating)/(numberTimesRated + 1));
                        recipeRateBar.setRating(newRating);
                        parseObject.put("Rating", newRating);
                        parseObject.put("HowManyTimesRated", (numberTimesRated + 1));
                        parseObject.put("RatingSum", (ratingSum+userRating));
                        parseObject.saveInBackground();

                    }
                }
            );

        }

    }


        //used in get recipe details; runs a second tread to load the image with url

    public class LoadImageFromURL extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... src) {
            // TODO Auto-generated method stub

            try {
                URL url = new URL(src[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //check if ImageView is null before setting incase user has left page
            if (iv == null) {
                return;
            }
            else {
                iv.setImageBitmap(result);
            }
        }

    }


    //this is the function to get all the details to populate this view
    void getRecipeDetails(final View v){

        final MainActivity activity = (MainActivity) getActivity();
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
                    parseObject = recipe;
                    // getting recipe title and changing font
                    TextView recipeTitleBox = (TextView) v.findViewById(R.id.recipeTitle);
                    Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "Pacifico.ttf");
                    recipeTitleBox.setTypeface(typeFace);
                    recipeTitleBox.setText(recipe.get("Name").toString());

                    // getting recipe description and changing font
                    TextView recipeDetailBox = (TextView) v.findViewById(R.id.recipeDetails);
                    Typeface typeFace2 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
                    recipeDetailBox.setTypeface(typeFace2);
                    recipeDetailBox.setText(recipe.get("Description").toString());

                    // getting recipe ingredients and changing font
                    TextView recipeIngredientsBox = (TextView) v.findViewById(R.id.recipeIngredients);
                    recipeIngredientsBox.setTypeface(typeFace2);
                    recipeIngredientsBox.setText(recipe.get("Ingredients").toString());

                    // getting recipe instructions and changing font
                    TextView recipeDirectionBox = (TextView) v.findViewById(R.id.recipeInstructions);
                    recipeDirectionBox.setTypeface(typeFace2);
                    recipeDirectionBox.setText(recipe.get("Directions").toString());

                   //getting recipe image url and loading the image with url
                    String url = recipe.get("img").toString();
                    iv = (ImageView) v.findViewById(R.id.imageView2);
                    LoadImageFromURL loadImage = new LoadImageFromURL();
                    loadImage.execute(url);

                    // getting recipe rating
                    oldRating = Float.parseFloat(recipe.get("Rating").toString());
                    ratingSum = Float.parseFloat(recipe.get("RatingSum").toString());
                    numberTimesRated = Integer.parseInt(recipe.get("HowManyTimesRated").toString());
                    recipeRateBar = (RatingBar) v.findViewById(R.id.ratingBar);
                    recipeRateBar.setRating(oldRating);



                }


            }
        });



    }

    void fillInViewContents(View v) {
        //TextView recipeTitleBox = (TextView) this.v.findViewById(R.id.recipeTitle);
        //recipeTitleBox.setText(this.recipeName.toUpperCase());
    }
}
