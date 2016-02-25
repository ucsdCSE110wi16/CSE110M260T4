package com.ucsd.cse110.recipeforsuccess;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import com.parse.*;
import java.util.*;
import android.util.Log;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;



public class DatabaseTest {

    @Before
    public void setUp() throws Exception {
        //do something here that all the tests can leverage
    }

    @Test
    public void testHelloWorld() throws Exception {
        Assert.assertEquals(true, true);
    }

    public class MainActivity extends AppCompatActivity {

        private static Boolean bParseInitialized = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            if (bParseInitialized == false) {

                Parse.enableLocalDatastore(this);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Recipe");
                query.whereEqualTo("Name", "Macaroni and Cheese");

                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> Ingredients, ParseException e) {
                        if (e == null) {
                            Log.d("", "Retrieved " + Ingredients.toString() + " Macaroni and Cheese index");
                        } else {
                            Log.d("Macaroni and Cheese", "Error: " + e.getMessage());
                        }
                    }
                })
                ;

                MainFragment mainFragment = new MainFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment)
                        .commit();

                bParseInitialized = true;
            }

            handleIntent(getIntent());
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


    }
}