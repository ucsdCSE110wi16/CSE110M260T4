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
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    private static Boolean bParseInitialized = false;

    //@Override
    // This can be used for loading our database
    protected void onCreate(Bundle savedInstanceState, ParseObject objectName) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ( bParseInitialized == false ) {

            Parse.enableLocalDatastore(this);

            Parse.initialize(this);
            ParseObject newObject = new ParseObject(objectName.toString());

            //TODO: Discuss options for passing in data that should be put in the database
            newObject.put("foo", "bar");
            newObject.saveInBackground();

            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment)
                    .commit();

            bParseInitialized = true;
        }

        handleIntent(getIntent());
    }

    //This will be used for querying out database
    //
    //TODO: The return will need to be changed to handle the data
    //
    //TODO: The parameter will also need to be changed depending on how many strings are queried
    public void queryDatabse(java.util.List<ParseObject> object, ParseException exception){
        if (exception == null) {
        } else{
            String objectToQuery = object.toString();
            //TODO: meet and figure out how to pass objects to be queried
            //ParseOuery queryObject = new ParseOuery(objectToQuery);
        }
    }


    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();

            if( extras.containsKey(RecipeSearchActivity.EXTRA_MESSAGE)  ) {
                String selectedRecipe = intent.getStringExtra(RecipeSearchActivity.EXTRA_MESSAGE);
                Toast.makeText(this, selectedRecipe + " selected", Toast.LENGTH_LONG).show();

                //Start the detail recipe view fragment
                RecipeDetailViewFragment fragment = new RecipeDetailViewFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            } else if( extras.containsKey(MainFragment.SEARCH_TERM) ) {
                String queryString = intent.getStringExtra(MainFragment.SEARCH_TERM);
                Toast.makeText(this, queryString + " searched", Toast.LENGTH_LONG).show();

                Intent new_intent = new Intent(MainActivity.this, RecipeSearchActivity.class);
                new_intent.setAction(Intent.ACTION_SEARCH);
                new_intent.putExtra(SearchManager.QUERY, queryString);
                startActivity(new_intent);
            }
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

}
