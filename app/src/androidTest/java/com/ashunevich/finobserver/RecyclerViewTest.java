package com.ashunevich.finobserver;

import android.util.Log;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith (AndroidJUnit4.class)
public class RecyclerViewTest {

    /** the {@link RecyclerView}'s resource id */
    private final int resId = R.id.accountView;
    RecyclerView recyclerView ;
    LinearLayoutManager manager;

    /** item count */
    private  int itemCount = 0;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){
        activityRule.getScenario().onActivity(activity -> {
             recyclerView = activity.findViewById(resId);
             manager = (LinearLayoutManager) recyclerView.getLayoutManager ();
             itemCount = Objects.requireNonNull (recyclerView.getAdapter ()).getItemCount ();
        });

    /* obtaining handles to the Ui of the Activity */
}

    @Test
    public void testListVisibleOnLaunch() {
        onView(withId(this.resId)).check (matches(isDisplayed ()));
    }

    //Scroll to end
    @Test
    public void scrollToEnd(){
        onView(withId(this.resId)).
                perform (RecyclerViewActions.scrollToPosition
                        (itemCount-1));
        Log.d("ITEM_COUNT",String.valueOf (itemCount-1));
    }

    //Scroll to next after last visible pos
    @Test
    public void scrollToNextCompletelyVisibleItemPosition() {
        onView(withId(this.resId)).
                perform (RecyclerViewActions.scrollToPosition(manager.findLastCompletelyVisibleItemPosition ()+1));
        Log.d("LAST_VISIBLE_POS",String.valueOf (manager.findLastCompletelyVisibleItemPosition()));
        Log.d("POS_AFTER_LAST_VISIBLE_ITEM",String.valueOf (manager.findLastCompletelyVisibleItemPosition()+1));
    }

    //Scroll to 6th element
    @Test
    public void scrollToPos() {
        onView(withId(this.resId)).
                perform (RecyclerViewActions.scrollToPosition(6));
        Log.d("LAST_VISIBLE_POS",String.valueOf (manager.findLastCompletelyVisibleItemPosition()));
    }
}
