package com.ucsd.cse110.recipeforsuccess;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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

import com.parse.Parse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Boolean bParseInitialized = false;
    private String curSelectedRecipeName = null;
    private String curSelectedObjectId = null;

    Button byIngredientButton;
    Button byRecipeButton;

    @Override
    // This can be used for loading our database
    protected void onCreate(Bundle savedInstanceState/*, ParseObject objectName*/) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        byIngredientButton.setOnClickListener(this);
        byIngredientButton.setWidth(buttonWidth);

        //setup the recipe button
        byRecipeButton = (Button) findViewById(R.id.byRecipeButton);
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
        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment)
                .commit();
    }

    /**
     * Method to bring in the ingredient search into view
     */
    private void setIngredientSearchActive(){
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
            if( extras.containsKey(RecipeSearchActivity.RECIPE_TITLE)  ) {

                String selectedRecipeTitle = intent.getStringExtra(RecipeSearchActivity.RECIPE_TITLE);
                String selectedRecipeObjectId = intent.getStringExtra(RecipeSearchActivity.RECIPE_OBJECT_ID);

                //set the values of the current selected items
                this.curSelectedRecipeName = selectedRecipeTitle;
                this.curSelectedObjectId = selectedRecipeObjectId;

                //hide the buttons
                hideButtons(true);

                //Start the detail recipe view fragment
                RecipeDetailViewFragment fragment = new RecipeDetailViewFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
            // This catches the return from the recipe name search view,
            // then launches the search activity passing it the search term.
            else if( extras.containsKey(MainFragment.SEARCH_TERM) ) {

                String queryString = intent.getStringExtra(MainFragment.SEARCH_TERM);
                queryString = queryString.toLowerCase();

                //hide the buttons
                hideButtons(false);

                Intent new_intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
                new_intent.setAction(Intent.ACTION_SEARCH);
                new_intent.putExtra(SearchManager.QUERY, queryString);
                startActivity(new_intent);
            }
            // This catches the return from the ingredients search view,
            // then launches the search activity passing it the searched ingredients
            else if( extras.containsKey(IngredientSearchViewFragment.INGREDIENTS_SEARCHED) ) {

                String[] queryIngredients = intent.getStringArrayExtra(IngredientSearchViewFragment.INGREDIENTS_SEARCHED);

                //lower case all the ingredients
                for (int i=0; i<queryIngredients.length ; i++ ) {
                    queryIngredients[i] = queryIngredients[i].toLowerCase();
                }
                //hide the buttons
                hideButtons(false);

                //launch the recipe search list with the ingredients list to search for
                Intent new_intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
                new_intent.setAction(Intent.ACTION_SEARCH);
                new_intent.putExtra(RecipeDetailViewFragment.INGREDIENTS_QUERY, queryIngredients);
                startActivity(new_intent);
            }
        }
        else if(RecipeSearchActivity.ACTION_NO_RESULTS.equals(intent.getAction())) {
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
