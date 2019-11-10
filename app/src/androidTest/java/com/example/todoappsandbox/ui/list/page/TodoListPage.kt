package com.example.todoappsandbox.ui.list.page

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.todoappsandbox.R
import org.junit.Test

object TodoListPage {

    @Test
    fun assertTitle(name: String) {
        val title = Espresso.onView(
            withIndex(withId(R.id.title_text), 0)
        )
        title.check(matches(withText(name)))
    }

    @Test
    fun assertDescription(name: String) {
        val description = Espresso.onView(
            withIndex(withId(R.id.description_text), 0)
        )
        description.check(matches(withText(name)))
    }

    fun goNewTodoPage(): NewTodoPage {
        val fab = Espresso.onView(withId(R.id.fab))
        fab.perform(click())
        return NewTodoPage
    }
}