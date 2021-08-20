package com.ashunevich.finobserver;

import android.view.View;

import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class TransactionRecyclerViewTest {

    /** the {@link RecyclerView}'s resource id */
    private final int resId = R.id.transactionView;
    private final int tabLayoutId = R.id.tabLayout;
    RecyclerView recyclerView ;
    LinearLayoutManager manager;
    TabLayout tabLayout;
    /** item count */
    private  int itemCount = 0;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException {
        onView(withId (this.tabLayoutId)).perform (selectTabAtPosition (1));
        onView(withId(R.id.loadingText)).perform (setViewVisibility (true));
        onView(withId(R.id.progressBar)).perform (setViewVisibility (true));
        activityRule.getScenario().onActivity(activity -> {
            recyclerView = activity.findViewById(resId);
            manager = (LinearLayoutManager) recyclerView.getLayoutManager ();
            itemCount = Objects.requireNonNull (recyclerView.getAdapter ()).getItemCount ();
            tabLayout = activity.findViewById (tabLayoutId);
        });
        Thread.sleep (450);
        }

    @Test
    public void testRecViewIsDispalyed() {
        onView(withId(this.resId)).perform (setViewVisibility (true));
        onView(withId(this.resId)).check(matches(isDisplayed ()));
    }

    @Test
    public void testHidingAnimation() {
        onView(withId(R.id.loadingText)).perform (setViewVisibility (false));
        onView(withId(R.id.progressBar)).perform (setViewVisibility (false));
    }

    @Test
    public void testScrollToEnd() {
        onView(withId(this.resId)).perform (setViewVisibility (true)).perform (scrollToPosition
                (itemCount-1));
    }


    //custom matcher for TabLayout
    @NonNull
    private static ViewAction selectTabAtPosition(final int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + String.valueOf(position);
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TabLayout) {
                    TabLayout tabLayout = (TabLayout) view;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);

                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        };
    }


    private static ViewAction setViewVisibility(final boolean value) {
        return new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(View.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.setVisibility(value ? View.VISIBLE : View.GONE);
            }

            @Override
            public String getDescription() {
                return "Show / Hide View";
            }
        };
    }

}
