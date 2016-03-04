package com.ucsd.cse110.recipeforsuccess;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.anything;
import android.support.test.espresso.Espresso;
/**
 * Basic tests showcasing simple view matchers and actions like {@link ViewMatchers#withId},
 * {@link ViewActions#click} and {@link ViewActions#typeText}.
 * <p>
 * Note that there is no need to tell Espresso that a view is in a different {@link Activity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchByPartialTitleScenario {


    public static final String RECIPE_TITLE = "chicken kebabs";
    public static final String STRING_TO_BE_TYPED = "chicken";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void searchByKeyword() {
        // Click the search edit box and then type text
        onView(withId(R.id.editText))
                .perform(click())
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // Click the search button
        onView(withId(R.id.button))
                .perform(click());

        //Click the item we are looking for
        onData(allOf(is(instanceOf(RecipeSearchActivity.MyListItem.class)), is(hasToString("chicken kebabs"))))
                .perform(click());


        //Confirm we got the recipe detail page
        onView(withId(R.id.recipeTitle))
                .check(matches(withText(RECIPE_TITLE)));


    }
}