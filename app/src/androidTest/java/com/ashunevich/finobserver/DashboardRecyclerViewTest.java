package com.ashunevich.finobserver;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ashunevich.finobserver.utility.Utils.countingIdlingResource;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class DashboardRecyclerViewTest {

    /** the {@link RecyclerView}'s resource id */
    private final int resId = R.id.accountView;
    RecyclerView recyclerView ;
    LinearLayoutManager manager;
    /** item count */
    private  int itemCount = 0;
    IdlingResource resource = countingIdlingResource();

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup(){
        /* obtaining handles to the Ui of the Activity */
        activityRule.getScenario().onActivity(activity -> {
             recyclerView = activity.findViewById(resId);
             manager =(LinearLayoutManager) recyclerView.getLayoutManager();
             itemCount = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
        });
        IdlingRegistry.getInstance().register(resource);
    }

    @After
    public void after(){
        IdlingRegistry.getInstance().unregister (resource);
    }

    //RecViewTest
    @Test
    public void testListVisibleOnLaunch() {
        onView(withId(this.resId)).check(matches(isDisplayed()));
    }

    //Scroll to end
    @Test
    public void scrollToEnd(){
        RecyclerViewPageObject robot = new RecyclerViewPageObject (resId);
        robot.scrollToBottom(itemCount);
        Log.d("ITEM_COUNT",String.valueOf(itemCount-1));
    }

    //Scroll to next after last visible pos
    @Test
    public void scrollToNextCompletelyVisibleItemPosition() {
        RecyclerViewPageObject robot = new RecyclerViewPageObject (resId);
        robot.scrollToItemAt(manager.findLastCompletelyVisibleItemPosition()+1);
        //decompilator java -> bytecod
        Log.d("LAST_VISIBLE_POS",String.valueOf(manager.findLastCompletelyVisibleItemPosition()));
        Log.d("POS_AFTER_LAST_VISIBLE_ITEM",String.valueOf(manager.findLastCompletelyVisibleItemPosition()+1));
    }

    //Scroll to 6th element
    @Test
    public void scrollToPos() {
        RecyclerViewPageObject recViewPom = new RecyclerViewPageObject (resId);
        recViewPom.scrollToItemAt(6);
        Log.d("LAST_VISIBLE_POS",String.valueOf(manager.findLastCompletelyVisibleItemPosition()));
    }
//

    @Test
    public void scrollAndCheck() {
        RecyclerViewPageObject recViewPom = new RecyclerViewPageObject (this.resId);
        recViewPom.checkIfItemExist("name 99");
    }

    @Test
    public void replaceTextKnowingPos() {
        RecyclerViewPageObject recViewPom = new RecyclerViewPageObject (this.resId);
        recViewPom.clickItemAt (7);

        EditTextPageObject editTextPageObject = new EditTextPageObject ();
        editTextPageObject.replaceTextOnOpen("name 99","2500.00");

        BasePageObject robotBase = new BasePageObject (R.id.okButton);
        robotBase.clickOnView();


/*


 */
    }

    }
