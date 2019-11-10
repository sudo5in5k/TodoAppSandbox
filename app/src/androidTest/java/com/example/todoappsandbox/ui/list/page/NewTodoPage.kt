package com.example.todoappsandbox.ui.list.page

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.todoappsandbox.R
import org.hamcrest.Matchers

object NewTodoPage {

    fun edit(): NewTodoPage {
        val title = Espresso.onView(ViewMatchers.withId(R.id.register_title))
        title.perform(
            ViewActions.replaceText(PageConst.INPUT_TITLE),
            ViewActions.closeSoftKeyboard()
        )

        val description = Espresso.onView(ViewMatchers.withId(R.id.register_description))
        description.perform(
            ViewActions.replaceText(PageConst.INPUT_DESCRIPTION),
            ViewActions.closeSoftKeyboard()
        )
        return NewTodoPage
    }

    fun save(): TodoListPage {
        val saveMenu = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.save), ViewMatchers.withContentDescription("SAVE"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.action_bar),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        saveMenu.perform(ViewActions.click())
        return TodoListPage
    }
}