package com.ucsd.cse110.recipeforsuccess;

import android.app.ListActivity;
import android.app.SearchManager;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecipeSearchActivity extends ListActivity {

    public final static String EXTRA_MESSAGE = "com.ucsd.cse110.recipeforsuccess.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void setListData(String[] searchResults) {

        ArrayAdapter<String> adapter=new
                ArrayAdapter<String>(
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
        String item = (String) getListAdapter().getItem(position);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(EXTRA_MESSAGE, item);
        startActivity(intent);
    }

    private void doMySearch(String query) {

        //query is the string that the user searched for

        //do the actual search of the database...
        String[]  fakeSearchResults={"A","B","C"};

        //set list data
        setListData(fakeSearchResults);
    }
}
