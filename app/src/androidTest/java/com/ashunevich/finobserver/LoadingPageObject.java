package com.ashunevich.finobserver;

import androidx.test.espresso.matcher.ViewMatchers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.ashunevich.finobserver.CustomMatchersUtils.setViewVisibility;

public class LoadingPageObject {
    int loadingText = R.id.loadingText;
    int progressBar = R.id.progressBar;

    public LoadingPageObject (){
    }

    public void setVisibleAndCheck(){
        onView(withId(loadingText)).perform(setViewVisibility(true)).
                check(matches(isDisplayed()));
        onView(withId(progressBar)).perform(setViewVisibility(true)).
                check(matches(isDisplayed()));
    }

    public void hideAndCheck(){
        onView(withId(loadingText)).
                perform(setViewVisibility(false)).
                check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        onView(withId(progressBar)).
                perform(setViewVisibility(false)).
                check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }



}
