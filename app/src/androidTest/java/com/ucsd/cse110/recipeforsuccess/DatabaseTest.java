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

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;
import com.parse.ParseException;
import com.parse.*;
import android.util.Log;

public class DatabaseTest {

    @Before
    public void setUp() throws Exception {
        //do something here that all the tests can leverage
    }

    @Test
    public void testHelloWorld() throws Exception {
        Assert.assertEquals(true, true);

        //Parse.enableLocalDatastore(this);

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
    }
}