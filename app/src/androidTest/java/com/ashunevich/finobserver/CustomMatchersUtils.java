package com.ashunevich.finobserver;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.ashunevich.finobserver.adapters.DashboardRecyclerViewAdapter;
import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.internal.util.Checks;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;


public class CustomMatchersUtils {

	static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}

	protected static ViewAction setViewVisibility(final boolean value) {
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

	@NonNull
	protected static ViewAction selectTabAtPosition(final int position) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
			}

			@Override
			public String getDescription() {
				return "with tab at index" + position;
			}

			@Override
			public void perform(UiController uiController, View view) {
				if(view instanceof TabLayout) {
					TabLayout tabLayout =(TabLayout) view;
					TabLayout.Tab tab = tabLayout.getTabAt(position);

					if(tab != null) {
						tab.select();
					}
				}
			}
		};
	}

	public static Matcher<RecyclerView.ViewHolder> withItemSubject(final String subject) {
		Checks.checkNotNull(subject);
		return new BoundedMatcher<RecyclerView.ViewHolder, DashboardRecyclerViewAdapter.DashboardViewHolder>(
				DashboardRecyclerViewAdapter.DashboardViewHolder.class) {

			@Override
			protected boolean matchesSafely(DashboardRecyclerViewAdapter.DashboardViewHolder viewHolder) {
				TextView subjectTextView = viewHolder.itemView.findViewById(R.id.accountType);

				return ((subject.equals(subjectTextView.getText().toString())));
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("item with subject: " + subject);
			}
		};
	}


}
