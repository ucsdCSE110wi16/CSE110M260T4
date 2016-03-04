package com.ucsd.cse110.recipeforsuccess;

import android.app.ListActivity;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;
import com.parse.ParseException;
import com.parse.*;
import android.util.Log;

public class RecipeSearchActivity extends ListActivity {

    public final static String RECIPE_TITLE = "com.ucsd.cse110.recipeforsuccess.RECIPE_TITLE";
    public final static String RECIPE_OBJECT_ID = "com.ucsd.cse110.recipeforsuccess.RECIPE_OBJECT_ID";
    //public final  View v;
    public class MyListItem {
        private String recipeTitle;
        private String objectID;

        public void setTitle(String title) {
            this.recipeTitle = title;
        }

        public void setObjectId(String id){
            this.objectID = id;
        }

        public String getTitle() {
            return this.recipeTitle;
        }

        public String getObjectID() {
            return this.objectID;
        }

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

        ArrayAdapter<MyListItem> adapter=new
                ArrayAdapter<MyListItem>(
                this,
                android.R.layout.simple_list_item_1,
                searchResults);
        setListAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        
        MyListItem item = (MyListItem) getListAdapter().getItem(position);

        Intent intent = new Intent(this,MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(RECIPE_TITLE, item.getTitle());

        //should this be here?
        intent.putExtra(RECIPE_OBJECT_ID, item.getObjectID());
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
                     //TextView recipeRateBar = (TextView) v.findViewById(R.id.textView);
                     MyListItem[] blankListItems = new MyListItem[1];
                     MyListItem blankListItem = new MyListItem();
                     blankListItem.setTitle("Sorry There Were No Recipes Found Matching Your Search");
                     blankListItems[0] = blankListItem;
                     //set list data
                     setListData(blankListItems);

                }
                //if recipes are found matching search
                else {
                    int num_search_results = recipeList.size();

                    MyListItem[] listItems = new MyListItem[num_search_results];

                    int i = 0;

                    for(ParseObject recipe : recipeList ) {
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
