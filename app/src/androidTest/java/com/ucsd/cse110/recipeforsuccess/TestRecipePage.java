package com.ucsd.cse110.recipeforsuccess;

import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


public class TestRecipePage {

    public static final String STRING_TO_BE_TYPED = "chocolate chip cookies";
    public static final String RECIPE_TITLE = "chocolate chip cookies";


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void confirmRecipePage() throws InterruptedException {
        // Click the search edit box and then type text
        onView(withId(R.id.editText))
                .perform(click())
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.button))
                    .perform(click());

        Thread.sleep(3000);


        //Click the first item
        onData(allOf(is(instanceOf(RecipeSearchActivity.MyListItem.class)), is(hasToString("chocolate chip cookies"))))
                    .perform(click());


        Thread.sleep(2000);
        //Confirm we got the recipe detail page

        onView(allOf(withText(RECIPE_TITLE))).perform(click());
        Thread.sleep(2000);
        //checks all elements are on the recipe page and scrolls the page
        onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
        onView(withId(R.id.recipeTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed())).perform(ViewActions.swipeUp());
        Thread.sleep(2000);
        onView(withId(R.id.recipeDetails)).check(matches(isDisplayed())).perform(ViewActions.swipeUp());
        Thread.sleep(2000);
        onView(withId(R.id.recipeIngredients)).perform(ViewActions.scrollTo());
        Thread.sleep(2000);
        onView(withId(R.id.recipeIngredients)).check(matches(isDisplayed()));
        Thread.sleep(2000);
        onView(withId(R.id.recipeInstructions)).perform(ViewActions.scrollTo());
        onView(withId(R.id.recipeInstructions)).check(matches(isDisplayed()));

    }
}
