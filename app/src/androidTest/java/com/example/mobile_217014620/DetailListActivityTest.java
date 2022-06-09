package com.example.mobile_217014620;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailListActivityTest {

    @Rule
    public ActivityScenarioRule<DetailListActivity> mActivityScenarioRule = new ActivityScenarioRule<>(DetailListActivity.class);

    @Test
    public void detailListTextViewTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.title_name), withSubstring("Name"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withSubstring("Name")));
    }

    public void detailListButtonTest() {
        ViewInteraction button = onView(
                allOf(withId(R.id.button_upload), withText("UPLOAD"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button1 = onView(
                allOf(withId(R.id.button_map), withText("MAP"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button1.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.button_cmt), withText("COMMENT"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
    }
}
