package com.ashunevich.finobserver;


import androidx.test.espresso.action.ViewActions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class EditTextPageObject {
	int accountViewId = R.id.newAccountName;
	int valueViewId = R.id.newAccountValue;

	public EditTextPageObject () {
	}

	public void replaceTextOnOpen(String newAccountName, String newAccountValue) {
		onView(withId(accountViewId)).
				check(matches(isDisplayed())).perform(replaceText(newAccountName)).perform(closeSoftKeyboard());
		onView(withId(valueViewId)).check(matches(isDisplayed())).perform(replaceText(newAccountValue)).perform(closeSoftKeyboard());
	}


}
