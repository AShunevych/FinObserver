package com.ashunevich.finobserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.ashunevich.finobserver.CustomMatchersUtils.selectTabAtPosition;
import static com.ashunevich.finobserver.utility.Utils.countingIdlingResource;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class TransactionRecyclerViewTest {

    /**
     * the {@link RecyclerView}'s resource id
     */
    public final int resId = R.id.transactionView;
    private final int tabLayoutId = R.id.tabLayout;
    private final int viewPagerId = R.id.viewPager;

    IdlingResource resource = countingIdlingResource();
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    /**
     * item count
     */
    private int itemCount = 0;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        onView(withId(tabLayoutId)).perform(selectTabAtPosition(1));
        onView(withId(viewPagerId)).perform(swipeLeft());

        activityRule.getScenario().onActivity(activity -> {
            recyclerView = activity.findViewById(resId);
            manager =(LinearLayoutManager) recyclerView.getLayoutManager();
            itemCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
        });
        IdlingRegistry.getInstance().register(resource);
    }

    @After
    public void teardown() {
        IdlingRegistry.getInstance().unregister(resource);
    }

    @Test
    public void testRecViewIsDisplayed() {
        RecyclerViewPageObject robot = new RecyclerViewPageObject (resId);
        robot.checkIfDisplayed();
    }

    @Test
    public void testHidingAnimation() {

        LoadingPageObject loadingPageObject = new LoadingPageObject ();
        loadingPageObject.setVisibleAndCheck();

        RecyclerViewPageObject robot = new RecyclerViewPageObject (resId);
        robot.checkIfDisplayed();
        robot.scrollToBottom(itemCount);

        loadingPageObject.hideAndCheck();

    }


}
