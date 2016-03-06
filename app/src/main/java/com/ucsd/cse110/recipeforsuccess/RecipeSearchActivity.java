package com.ucsd.cse110.recipeforsuccess;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecipeSearchActivity extends ListActivity {

    //public final  View v;

    /**
     * This is an adapter for the search results received from Parse
     * so that it can be displayed in the ListActivity.
     */
    public class MyListItem {
        private String recipeTitle;
        private String objectID;

        /**
         * Setter
         *
         * @param title title (will be what is displaed in the list
         */
        public void setTitle(String title) {
            this.recipeTitle = title;
        }

        /**
         * Setter
         *
         * @param id - object ID retreived from parse.
         */
        public void setObjectId(String id){
            this.objectID = id;
        }

        /**
         * Getter
         *
         * @return - Title of the recipe
         */
        public String getTitle() {
            return this.recipeTitle;
        }

        /**
         * Getter
         *
         * @return object ID from parse
         */
        public String getObjectID() {
            return this.objectID;
        }

        /**
         * Called by the list to get what to be displayed
         *
         * @return Name of the recipe
         */
        @Override
        public String toString() {
            return this.recipeTitle;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());

    }

    private void setListData(MyListItem[] searchResults) {

//        ArrayAdapter<MyListItem> adapter=new
//                ArrayAdapter<MyListItem>(
//                this,
//                android.R.layout.simple_list_item_1,
//                searchResults);
//        setListAdapter(adapter);

        // adapter for using custom font
        ArrayAdapter<MyListItem> listAdapter = new CustomListAdapter(this, android.R.layout.simple_list_item_1, searchResults);
        setListAdapter(listAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handles the intent of the starting of this activity.  Will be launched with either
     * a recipe title search or ingredient search.
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();

            if( extras.containsKey(SearchManager.QUERY)  ) {
                /**
                 * this is the result of search by recipe name
                 */
                String query = intent.getStringExtra(SearchManager.QUERY);
                doMySearch(query);
            }
            else if (extras.containsKey(MainActivity.INGREDIENTS_SEARCHED)  ) {
                /**
                 * this is the result of search by ingredients
                 */
                String [] ingredients = intent.getStringArrayExtra(MainActivity.INGREDIENTS_SEARCHED);

                //just doing a toast here to confirm string of ingredients was passed.
                //String ingredients_str = "";
                //for(String ingredient : ingredients)
                //    ingredients_str += ingredient;

                //Toast.makeText(getApplicationContext(), ingredients_str, Toast.LENGTH_LONG).show();

                doIngSearch(ingredients);
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        
        MyListItem item = (MyListItem) getListAdapter().getItem(position);

        Intent intent = new Intent(this,MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(MainActivity.RECIPE_TITLE, item.getTitle());

        //should this be here?
        intent.putExtra(MainActivity.RECIPE_OBJECT_ID, item.getObjectID());
        startActivity(intent);
    }

    private void handleNoResultsFound(){

        Intent intent = new Intent(this,MainActivity.class);
        intent.setAction(MainActivity.ACTION_NO_RESULTS);
        startActivity(intent);
    }

    private void doMySearch(String query) {

        //query is the string that the user searched for
        //Toast.makeText(this, query + " has been passed to the search results", Toast.LENGTH_LONG).show();

        ParseQuery<ParseObject> search = ParseQuery.getQuery("Recipe");
        search.whereContains("Name", query);
        search.orderByAscending("Name");
        search.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> recipeList, ParseException e) {
                if (e == null) {
                    Log.d("recipe", "Retrieved " + recipeList.size() + "recipes");
                } else {
                    Log.d("recipe", "Error: " + e.getMessage());
                }


                //if no recipes are found matching search
                if (recipeList.size() == 0) {
                    handleNoResultsFound();
                }
                //if recipes are found matching search
                else {
                    int num_search_results = recipeList.size();

                    MyListItem[] listItems = new MyListItem[num_search_results];

                    int i = 0;

                    for (ParseObject recipe : recipeList) {
                        MyListItem listItem = new MyListItem();
                        listItem.setTitle(String.format("%s", recipe.get("Name")));
                        listItem.setObjectId(String.format("Object ID %s", recipe.getObjectId()));
                        listItems[i] = listItem;
                        i++;
                    }

                    //set list data
                    setListData(listItems);
                }
            }
        });
    }

    private void doIngSearch(String ingredients[]) {

        //query is the string that the user searched for
        //Toast.makeText(this, query + " has been passed to the search results", Toast.LENGTH_LONG).show();
        ArrayList<String> ingNames = new ArrayList<String>();

        for (int x = 0; x<ingredients.length; x++) {
           Log.d("ingredients", "Added " + ingredients[x] + " to ingredients");
           ingNames.add(ingredients[x]);
        }


        ParseQuery<ParseObject> ingSearch = ParseQuery.getQuery("Ingredients");
        ingSearch.whereContainedIn("Name", ingNames);
        ingSearch.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> ingList, ParseException e) {
                if (e == null) {
                    Log.d("ingredients", "Retrieved " + ingList.size() + " ingredients");
                } else {
                    Log.d("ingredients", "Error: " + e.getMessage());
                }


                //if no recipes are found matching search
                if (ingList.size() == 0) {
                    handleNoResultsFound();
                }
                //if recipes are found matching search
                else {

                    ArrayList<String> ingID = new ArrayList<>();

                    for (ParseObject ingredient : ingList) {
                        ingID.add(ingredient.getObjectId());
                        Log.d("ingredients", "object " + ingredient.getObjectId() + " added to ID list");
                    }

                    ParseQuery<ParseObject> recSearch = ParseQuery.getQuery("Recipe");
                    recSearch.whereContainsAll("IngKeys", ingID);
                    recSearch.orderByAscending("Name");
                    recSearch.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> recipeList, ParseException e) {
                            if (e == null) {
                                Log.d("ingredients", "Retrieved " + recipeList.size() + " recipes");
                            } else {
                                Log.d("ingredients", "Error: " + e.getMessage());
                            }


                            //if no recipes are found matching search
                            if (recipeList.size() == 0) {
                                handleNoResultsFound();
                            }
                            //if recipes are found matching search
                            else {
                                int num_search_results = recipeList.size();

                                MyListItem[] listItems = new MyListItem[num_search_results];

                                int i = 0;

                                for (ParseObject recipe : recipeList) {
                                    MyListItem listItem = new MyListItem();
                                    listItem.setTitle(String.format("%s", recipe.get("Name")));
                                    listItem.setObjectId(String.format("Object ID %s", recipe.getObjectId()));
                                    listItems[i] = listItem;
                                    i++;
                                }

                                //set list data
                                setListData(listItems);
                            }
                        }
                    });
                }
            }
        });
    }

}
