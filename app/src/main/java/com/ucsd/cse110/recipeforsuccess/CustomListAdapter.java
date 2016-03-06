package com.ucsd.cse110.recipeforsuccess;

import android.graphics.Typeface;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CustomListAdapter extends ArrayAdapter {

    public CustomListAdapter(Context context, int textViewResourceId, RecipeSearchActivity.MyListItem[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = super.getView(position, convertView, parent);

        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
        tv.setTypeface(typeface);

        return view;

    }
}
