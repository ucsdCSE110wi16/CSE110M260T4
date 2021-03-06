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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 *
 */
public class NoMatchesFoundIngredients {
    public static final String STRING_TO_BE_TYPED = "celery";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void searchByKeyword() {
        // Click on the "Search by Ingredients" button
        onView(withId(R.id.byIngredientButton))
                .perform(click());

        // Click the search edit box and then type text
        onView(withId(R.id.editText))
                .perform(click())
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Click the search button
        onView(withId(R.id.button))
                .perform(click());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Confirm that the recipe isn't found
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));


    }
}
