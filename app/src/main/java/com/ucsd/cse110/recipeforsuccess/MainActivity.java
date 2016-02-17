package com.ucsd.cse110.recipeforsuccess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseObject;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
        ParseObject testObject = new ParseObject("Ingredients");
        testObject.put("Name", "beef");
        testObject.saveInBackground();
        

    }
}
