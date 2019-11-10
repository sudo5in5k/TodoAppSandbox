package com.example.todoappsandbox.ui.list.page

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.todoappsandbox.R
import org.junit.Test

object TodoListPage {

    @Test
    fun assertTitle(name: String) {
        val title = Espresso.onView(
            withIndex(ViewMatchers.withId(R.id.title_text), 0)
        )
        title.check(ViewAssertions.matches(ViewMatchers.withText(name)))
    }

    @Test
    fun assertDescription(name: String) {
        val description = Espresso.onView(
            withIndex(ViewMatchers.withId(R.id.description_text), 0)
        )
        description.check(ViewAssertions.matches(ViewMatchers.withText(name)))
    }

    fun goNewTodoPage(): NewTodoPage {
        val fab = Espresso.onView(ViewMatchers.withId(R.id.fab))
        fab.perform(click())
        return NewTodoPage
    }
}