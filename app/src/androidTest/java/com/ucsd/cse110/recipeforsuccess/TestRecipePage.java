package com.ucsd.cse110.recipeforsuccess;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by ellimag on 3/4/16.
 */
public class TestRecipePage {

    public static final String STRING_TO_BE_TYPED = "mac n cheese";
    public static final String RECIPE_TITLE = "mac n cheese";
    public static final String STRING_TO_BE_TYPED2 = "chicken";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void confirmRecipePage() {
        // Click the search edit box and then type text
        onView(withId(R.id.editText))
                .perform(click())
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.button))
                .perform(click());

        //Click the first item
        onData(allOf(is(instanceOf(RecipeSearchActivity.MyListItem.class)), is(hasToString("mac n cheese"))))
                .perform(click());


        //Confirm we got the recipe detail page
        onView(withId(R.id.recipeTitle))
                .check(matches(withText(RECIPE_TITLE)));

        onView(allOf(withText(RECIPE_TITLE))).perform(click());
        //onView(withId(R.id.recipeTitle)).click()

        // perform top-level user actions
        //pressBack();
        //pressBack();

        //  Click the search edit box and then type text
        //onView(withId(R.id.search))
        //        .perform(click())
        //        .perform(typeText(STRING_TO_BE_TYPED2), closeSoftKeyboard());
    }
}
