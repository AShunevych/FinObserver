package com.ashunevich.finobserver;

import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ashunevich.finobserver.CustomMatchersUtils.setViewVisibility;
import static com.ashunevich.finobserver.CustomMatchersUtils.withItemSubject;


public class RecyclerViewPageObject {

	int id;

	public RecyclerViewPageObject (int id) {
		this.id = id;
	}

	public void checkIfDisplayed() {
		onView(withId(id)).
				perform(setViewVisibility(true)).
				check(matches(isDisplayed()));
	}

	public void scrollToBottom(int itemCount) {
		onView(withId(id)).perform(setViewVisibility(true)).perform(scrollToPosition(itemCount - 1));
	}

	public void scrollToItemAt(int itemPosition) {
		onView(withId(id)).
				perform(scrollToPosition(itemPosition));
	}

	//Video 5 55:56
	public void clickItemAt(int itemPosition) {
		onView(withId(id)).
				perform(RecyclerViewActions.actionOnItemAtPosition(itemPosition, click()));
	}

	public void scrollToItemWithText(String text) {
		onView(withId(id)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(text))));
	}




}
