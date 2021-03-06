package com.ucsd.cse110.recipeforsuccess;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.util.DisplayMetrics;
import android.graphics.Color;
import android.widget.TextView;

import com.parse.Parse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Boolean bParseInitialized = false;
    private String curSelectedRecipeName = null;
    private String curSelectedObjectId = null;

    Button byIngredientButton;
    Button byRecipeButton;

    public final static String INGREDIENTS_SEARCHED = "com.ucsd.cse110.recipeforsuccess.INGREDIENTS_SEARCHED";
    public final static String SEARCH_TERM = "com.ucsd.cse110.recipeforsuccess.SEARCH_TERM";
    public final static String RECIPE_TITLE = "com.ucsd.cse110.recipeforsuccess.RECIPE_TITLE";
    public final static String RECIPE_OBJECT_ID = "com.ucsd.cse110.recipeforsuccess.RECIPE_OBJECT_ID";
    public final static String ACTION_NO_RESULTS = "com.ucsd.cse110.recipeforsuccess.ACTION_NO_RESULTS";

    @Override
    // This can be used for loading our database
    protected void onCreate(Bundle savedInstanceState/*, ParseObject objectName*/) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFAAEFDF"));
        //getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        if (!bParseInitialized) {

            Parse.enableLocalDatastore(this);

            //this just needs to happen once per app launch.
            //this should stay in onCreate
            Parse.initialize(this);
            bParseInitialized = true;

        }

        //layout the buttons
        layoutButtons();

        setRecipeSearchActive();

        handleIntent(getIntent());
    }

    /**
     * Handles the setup of the buttons to switch between the recipe and ingredient search
     */
    private void layoutButtons(){
        // Use DisplayMetrics to get the width
        // so we can get the button to share half the screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int buttonWidth = width/2 + 10;

        //Setup the ingredient button
        byIngredientButton = (Button) findViewById(R.id.byIngredientButton);
        TextView ingredientButtonText = (TextView) findViewById(R.id.byIngredientButton);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Slabo13px-Regular.ttf");
        ingredientButtonText.setTypeface(typeface);
        byIngredientButton.setOnClickListener(this);
        byIngredientButton.setWidth(buttonWidth);

        //setup the recipe button
        byRecipeButton = (Button) findViewById(R.id.byRecipeButton);
        TextView byRecipeButtonText = (TextView) findViewById(R.id.byRecipeButton);
        byRecipeButtonText.setTypeface(typeface);
        byRecipeButton.setOnClickListener(this);
        byRecipeButton.setWidth(buttonWidth);

        //disable it because we are going to start with this tab
        byRecipeButton.setEnabled(false);
    }

    /**
     * Hide the recipe/ingredient search buttons
     */
    private void hideButtons(boolean hide){
        int visibility;
        if (hide) {
            visibility = View.GONE;
        } else {
            visibility = View.VISIBLE;
        }

        byRecipeButton.setVisibility(visibility);
        byIngredientButton.setVisibility(visibility);
    }

    /**
     * Method to bring in the recipe search into view
     */
    private void setRecipeSearchActive(){

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFAAEFDF"));

        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment)
                .commit();
    }

    /**
     * Method to bring in the ingredient search into view
     */
    private void setIngredientSearchActive(){

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFAAEFDF"));

        IngredientSearchViewFragment fragment = new IngredientSearchViewFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Method allowing the fragments to get current object id of the selected recipe
     */
    public String getCurSelectedObjectId() {
        return this.curSelectedObjectId;
    }

    /**
     * Method allowing the fragments to get current name of the selected recipe
     */
    public String getCurSelectedRecipeName() {
        return this.curSelectedRecipeName;
    }


    /**
     * Handler for passing into and back from the search activity.
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();

            //Catch the return from the search activity and launch recipe details
            if( extras.containsKey(RECIPE_TITLE)  ) {

                String selectedRecipeTitle = intent.getStringExtra(RECIPE_TITLE);
                String selectedRecipeObjectId = intent.getStringExtra(RECIPE_OBJECT_ID);

                //set the values of the current selected items
                this.curSelectedRecipeName = selectedRecipeTitle;
                this.curSelectedObjectId = selectedRecipeObjectId;

                //hide the buttons
                hideButtons(true);

                getWindow().getDecorView().setBackgroundColor(Color.WHITE);

                //Start the detail recipe view fragment
                RecipeDetailViewFragment fragment = new RecipeDetailViewFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
            // This catches the return from the recipe name search view,
            // then launches the search activity passing it the search term.
            else if( extras.containsKey(SEARCH_TERM) ) {

                String queryString = intent.getStringExtra(SEARCH_TERM);
                queryString = queryString.toLowerCase();

                //hide the buttons
                hideButtons(false);

                getWindow().getDecorView().setBackgroundColor(Color.WHITE);

                Intent new_intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
                new_intent.setAction(Intent.ACTION_SEARCH);
                new_intent.putExtra(SearchManager.QUERY, queryString);
                startActivity(new_intent);
            }
            // This catches the return from the ingredients search view,
            // then launches the search activity passing it the searched ingredients
            else if( extras.containsKey(INGREDIENTS_SEARCHED) ) {

                String[] queryIngredients = intent.getStringArrayExtra(INGREDIENTS_SEARCHED);

                //lower case all the ingredients
                for (int i=0; i<queryIngredients.length ; i++ ) {
                    queryIngredients[i] = queryIngredients[i].toLowerCase();
                }
                //hide the buttons
                hideButtons(false);

                getWindow().getDecorView().setBackgroundColor(Color.WHITE);

                //launch the recipe search list with the ingredients list to search for
                Intent new_intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
                new_intent.setAction(Intent.ACTION_SEARCH);
                new_intent.putExtra(INGREDIENTS_SEARCHED, queryIngredients);
                startActivity(new_intent);
            }
        }
        else if(ACTION_NO_RESULTS.equals(intent.getAction())) {

            getWindow().getDecorView().setBackgroundColor(Color.WHITE);

            //launch the recipe search list with the ingredients list to search for
            //Search button clicked display searching fragment view
            NotFoundFragment notFoundFragment = new NotFoundFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, notFoundFragment)
                    .commit();

            byRecipeButton.setEnabled(true);
            byIngredientButton.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                getWindow().getDecorView().setBackgroundColor(Color.WHITE);

                //Search button clicked display searching fragment view
                SearchingViewFragment searchingFragment = new SearchingViewFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, searchingFragment)
                        .commit();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do whatever you need
                getSupportFragmentManager().popBackStack();

                getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFAAEFDF"));

                MainFragment mainFragment = new MainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment)
                        .commit();
                return true;
            }

        });

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.byRecipeButton:
                setRecipeSearchActive();
                this.byRecipeButton.setEnabled(false);
                this.byIngredientButton.setEnabled(true);
                break;
            case R.id.byIngredientButton:
                setIngredientSearchActive();
                this.byRecipeButton.setEnabled(true);
                this.byIngredientButton.setEnabled(false);
                break;

        }
    }

}
