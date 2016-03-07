package com.ucsd.cse110.recipeforsuccess;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotFoundFragment extends Fragment {


    public NotFoundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_not_found, container, false);
        setText(v);

        // Required empty public constructor
        getActivity().getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        return v;
    }

    public void setText(View v) {
        TextView top = (TextView) v.findViewById(R.id.textView3);
        Typeface typeFace1 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
        top.setTypeface(typeFace1);
        top.setText("Sorry we couldn't find what you were looking for");

        TextView bottom = (TextView) v.findViewById(R.id.textView4);
        Typeface typeFace2 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
        bottom.setTypeface(typeFace2);
        bottom.setText("Please try another search");

    }

}
