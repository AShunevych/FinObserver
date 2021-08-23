package com.ashunevich.finobserver;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class BasePageObject {
    int id;

    public BasePageObject (int id){
        this.id = id;
    }

    public void clickOnView(){
        onView(withId(id)).perform(click());
    }


}
