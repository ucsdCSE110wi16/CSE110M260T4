package com.ucsd.cse110.recipeforsuccess;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Fragment to handle the search by ingredient.
 */
public class IngredientSearchViewFragment extends Fragment  implements View.OnClickListener {

    Button mAddButton;
    Button mSearchButton;
    EditText mSearchTerm;
    TextView mSearchingIngredients;

    public IngredientSearchViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_ingredient_search_view, container, false);

        // Changing font of title
        TextView screenTitle = (TextView) v.findViewById(R.id.textView2);
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "Pacifico.ttf");
        screenTitle.setTypeface(typeFace);

        // Changing font of "Search" button
        TextView buttonText = (TextView) v.findViewById(R.id.button);
        Typeface typeFace2 = Typeface.createFromAsset(getContext().getAssets(), "Slabo13px-Regular.ttf");
        buttonText.setTypeface(typeFace2);

        // Changing font of "Add" button
        TextView addIngredientText = (TextView) v.findViewById(R.id.addIngredient);
        addIngredientText.setTypeface(typeFace2);

        // Changing font of ingredients
        TextView ingredientText = (TextView) v.findViewById(R.id.searchingIngredients);
        ingredientText.setTypeface(typeFace2);

        mSearchingIngredients = (TextView) v.findViewById(R.id.searchingIngredients);
        mSearchTerm = (EditText) v.findViewById(R.id.editText);
        mSearchButton = (Button) v.findViewById(R.id.button);
        mAddButton = (Button) v.findViewById(R.id.addIngredient);

        mSearchButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);

        return v;
    }

    /**
     * Function handles the add button press.
     */
    private void handleAddPressed() {
        String ingredient = mSearchTerm.getText().toString();
        if( ingredient.length() == 0 ) {
            Toast.makeText(getContext(), "Please enter ingredient to search", Toast.LENGTH_LONG).show();
            return;
        }
        String current_ingredients = mSearchingIngredients.getText().toString();
        if (current_ingredients.length() == 0) {
            current_ingredients = ingredient;
        }
        else {
            current_ingredients += (", " + ingredient);
        }
        mSearchingIngredients.setText(current_ingredients);
        mSearchTerm.setText("");
    }

    /**
     * Handles the search button pressed.
     */
    private void handleSeachPressed() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        String query = mSearchingIngredients.getText().toString();
        String[] ingredients = query.split(",");
        intent.putExtra(MainActivity.INGREDIENTS_SEARCHED, ingredients);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //add selected
            case R.id.addIngredient:
                handleAddPressed();
                break;

            //search selected
            case R.id.button:
                handleSeachPressed();
                break;
        }
    }

}
