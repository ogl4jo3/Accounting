package com.ogl4jo3.accounting.ui.categoryMgmt

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ogl4jo3.accounting.R
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class IncomeCategoryAddFragmentTest {

    @Test
    fun emptyName_isNotSaved() {
        launchFragmentInContainer<IncomeCategoryAddFragment>(null, R.style.AppTheme)

        onView(withId(R.id.et_category_name)).perform(clearText())
        onView(withId(R.id.btn_add)).perform(click())

        onView(withId(R.id.btn_add)).check(matches(isDisplayed()))
    }

    @Test
    fun validCategory_navigatesBack() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val scenario = launchFragmentInContainer<IncomeCategoryAddFragment>(null, R.style.AppTheme)
        scenario.onFragment {
            navController.setGraph(R.navigation.main_graph)
            navController.setCurrentDestination(R.id.incomeCategoryAddFragment)
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.et_category_name)).perform(replaceText("收入類別-1"))
        onView(withId(R.id.btn_add)).perform(click())

        assertEquals(navController.currentDestination?.id, R.id.incomeCategoryMgmtFragment)
    }

}

