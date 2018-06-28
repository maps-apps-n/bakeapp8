package com.example.android.bakeapp8;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;



import android.support.test.runner.AndroidJUnit4;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.matcher.ViewMatchers.withText;




import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class Testing {


    private List<RecipesParcelableResp> recipList;
    private int position = 1;



    @Test
    public void checkData(){

        String recipeName = recipList.get(position).getName();
        onView((withId(R.id.recipe_title))).check(matches(withText(recipeName)));
    }
}
