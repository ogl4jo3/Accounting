package com.ogl4jo3.accounting.ui

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.close
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ogl4jo3.accounting.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateToAllFragmentsInDrawer() {
        // AccountListFragment
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.accountListFragment))
        onView(withId(R.id.drawer_layout)).perform(close())
        onView(withId(R.id.rv_account)).check(matches(isDisplayed()))
        // ExpenseCategoryMgmtFragment
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.expenseCategoryMgmtFragment))
        onView(withId(R.id.drawer_layout)).perform(close())
        onView(withId(R.id.rv_categories)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_add)).check(matches(isDisplayed()))
        // IncomeCategoryMgmtFragment
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START))).perform(open())
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.incomeCategoryMgmtFragment))
        onView(withId(R.id.drawer_layout)).perform(close())
        onView(withId(R.id.rv_categories)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_add)).check(matches(isDisplayed()))


        //TODO: add more navigate fragment test

    }

}

