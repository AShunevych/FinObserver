package com.ashunevich.finobserver;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith (AndroidJUnit4.class)
public class DashboardRecyclerViewTest {

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
        /* obtaining handles to the Ui of the Activity */
        activityRule.getScenario().onActivity(activity -> {
             recyclerView = activity.findViewById(resId);
             manager = (LinearLayoutManager) recyclerView.getLayoutManager ();
             itemCount = Objects.requireNonNull (recyclerView.getAdapter ()).getItemCount ();
        });

}

    //RecViewTest
    @Test
    public void testListVisibleOnLaunch() {
        onView(withId(this.resId)).check (matches(isDisplayed ()));
    }

    //Scroll to end
    @Test
    public void scrollToEnd(){
        onView(withId(this.resId)).
                perform (scrollToPosition
                        (itemCount-1));
        Log.d("ITEM_COUNT",String.valueOf (itemCount-1));
    }

    //Scroll to next after last visible pos
    @Test
    public void scrollToNextCompletelyVisibleItemPosition() {
        onView(withId(this.resId)).
                perform (scrollToPosition(manager.findLastCompletelyVisibleItemPosition ()+1));
        //decompilator java -> bytecod
        Log.d("LAST_VISIBLE_POS",String.valueOf (manager.findLastCompletelyVisibleItemPosition()));
        Log.d("POS_AFTER_LAST_VISIBLE_ITEM",String.valueOf (manager.findLastCompletelyVisibleItemPosition()+1));
    }

    //Scroll to 6th element
    @Test
    public void scrollToPos() {
        onView(withId(this.resId)).
                perform (scrollToPosition(6));
        Log.d("LAST_VISIBLE_POS",String.valueOf (manager.findLastCompletelyVisibleItemPosition()));
    }
//

    @Test
    public void scrollAndCheck() {
        //To get item with "name 3" RecyclerView should scroll to the bottom -->
        // item doesn't created it yet when we only created app and see item #7 - #4
        onView(withId(this.resId)).
                perform (scrollToPosition
                        (itemCount-1)).perform(RecyclerViewActions.scrollTo(
                hasDescendant(withText("name 3"))
        ));
    }

    @Test
    public void getItemOnStart() {
        onView(withId(this.resId))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("name 7"))
                ));
    }

    //TestRecorderCode
    @Test
    public void clickItem() {
        onView(withId(this.resId)).perform (scrollToPosition (itemCount-1));
        onView (allOf (withId (this.resId), childAtPosition (withId (R.id.DashboardLayout), 7))).
                perform (actionOnItemAtPosition (4, click ()));
        onView (allOf (withId (R.id.cancelButton), withText ("Cancel"),
                childAtPosition (childAtPosition (withId (android.R.id.content), 0), 1), isDisplayed ())).perform (click ());
    }

    //SameCode
    @Test
    public void notTestRecorderClick() {
        onView(withId(this.resId)).perform (scrollToPosition (itemCount-1)).
                perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));
        onView(withId(R.id.cancelButton)).perform (click ());
    }

    @Test
    public void replaceText() {
        onView(withId(this.resId)).perform (scrollToPosition (itemCount-1)).
                perform(RecyclerViewActions.actionOnItemAtPosition(7, click()));
        onView (withId (R.id.newAccountName)).perform (ViewActions.replaceText ("name 8")).perform (ViewActions.closeSoftKeyboard ());
        onView (withId (R.id.newAccountValue)).perform (ViewActions.replaceText ("10.0")).perform (ViewActions.closeSoftKeyboard ());
        onView(withId(R.id.okButton)).perform (click ());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View> () {
            @Override
            public void describeTo(Description description) {
                description.appendText ("Child at position " + position + " in parent ");
                parentMatcher.describeTo (description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent ();
                return parent instanceof ViewGroup && parentMatcher.matches (parent)
                        && view.equals (((ViewGroup) parent).getChildAt (position));
            }
        };
    }
    }
