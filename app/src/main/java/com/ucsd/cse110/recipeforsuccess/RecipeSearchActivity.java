package com.ucsd.cse110.recipeforsuccess;

import android.app.ListActivity;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecipeSearchActivity extends ListActivity {

    public final static String RECIPE_TITLE = "com.ucsd.cse110.recipeforsuccess.RECIPE_TITLE";
    public final static String RECIPE_OBJECT_ID = "com.ucsd.cse110.recipeforsuccess.RECIPE_OBJECT_ID";

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

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(RECIPE_TITLE, item.getTitle());
        intent.putExtra(RECIPE_OBJECT_ID, item.getObjectID());
        startActivity(intent);
    }

    private void doMySearch(String query) {

        //query is the string that the user searched for

        int num_search_results = 5;

        MyListItem[] listItems = new MyListItem[num_search_results];

        for(int i=0; i<num_search_results; i++) {
            MyListItem listItem = new MyListItem();
            listItem.setTitle(String.format("Recipe Title %s", i));
            listItem.setObjectId(String.format("Object ID %s", i + 100));
            listItems[i] = listItem;
        }

        //set list data
        setListData(listItems);
    }
}
