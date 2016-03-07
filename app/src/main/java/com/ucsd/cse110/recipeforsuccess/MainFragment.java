package com.ucsd.cse110.recipeforsuccess;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainFragment extends Fragment implements OnClickListener{

    Button mSearchButton;
    EditText mSearchTerm;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFAAEFDF"));

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        if (v != null) {
            mSearchButton = (Button) v.findViewById(R.id.button);

            if (mSearchButton != null) {
                mSearchButton.setOnClickListener(this);
            }

            mSearchTerm = (EditText) v.findViewById(R.id.editText);

            // Changing font of title
            TextView screenTitle = (TextView) v.findViewById(R.id.textView2);
            Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "Pacifico.ttf");
            screenTitle.setTypeface(typeFace);

            // Changing font of button
            TextView buttonText = (TextView) v.findViewById(R.id.button);
            Typeface typeFace2 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
            buttonText.setTypeface(typeFace2);

        }
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                String query = mSearchTerm.getText().toString();
                intent.putExtra(MainActivity.SEARCH_TERM, query);
                startActivity(intent);
                break;

        }
    }
}
