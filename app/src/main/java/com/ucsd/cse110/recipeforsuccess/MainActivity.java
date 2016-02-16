package com.ucsd.cse110.recipeforsuccess;

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

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {

    private Boolean firstTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ( isFirstTime() ) {

            Parse.enableLocalDatastore(this);

            Parse.initialize(this);
            ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "bar");
            testObject.saveInBackground();
        }

        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mainFragment)
                .commit();

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String selectedRecipe = intent.getStringExtra(RecipeSearchActivity.EXTRA_MESSAGE);
            Toast.makeText(this, selectedRecipe + " selected", Toast.LENGTH_LONG).show();

            //Start the detail recipe view fragment
            RecipeDetailViewFragment fragment = new RecipeDetailViewFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
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

    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }

}
