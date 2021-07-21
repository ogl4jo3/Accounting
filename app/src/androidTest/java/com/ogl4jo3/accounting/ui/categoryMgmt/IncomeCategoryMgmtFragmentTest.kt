package com.ogl4jo3.accounting.ui.categoryMgmt

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ogl4jo3.accounting.R
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class IncomeCategoryMgmtFragmentTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun displayDefaultIncomeCategoryData() {
        launchFragmentInContainer<IncomeCategoryMgmtFragment>(null, R.style.AppTheme)

        onView(withText(context.getString(R.string.category_salary)))
            .check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.category_bonus)))
            .check(matches(isDisplayed()))
        onView(withText(context.getString(R.string.category_investment)))
            .check(matches(isDisplayed()))

        onView(ViewMatchers.withId(R.id.btn_add)).check(matches(isDisplayed()))
    }

}

