package com.example.todoappsandbox.ui.list.page

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.todoappsandbox.R
import org.hamcrest.Matchers

object NewTodoPage {

    fun edit(): NewTodoPage {
        val title = Espresso.onView(withId(R.id.register_title))
        title.perform(
            replaceText(PageConst.INPUT_TITLE),
            closeSoftKeyboard()
        )

        val description = Espresso.onView(withId(R.id.register_description))
        description.perform(
            replaceText(PageConst.INPUT_DESCRIPTION),
            closeSoftKeyboard()
        )
        return NewTodoPage
    }

    fun save(): TodoListPage {
        val saveMenu = Espresso.onView(
            Matchers.allOf(
                withId(R.id.save), withContentDescription("SAVE"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        saveMenu.perform(click())
        return TodoListPage
    }
}