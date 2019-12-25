package com.example.todoappsandbox.ui.list

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.todoappsandbox.ui.list.page.PageConst
import com.example.todoappsandbox.ui.list.page.TodoListPage.goNewTodoPage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddTodoTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<TodoActivity>()

    @Test
    fun addTodoTest() {
        val addTask = goNewTodoPage().edit().save()
        addTask.assertTitle(PageConst.INPUT_TITLE)
        addTask.assertDescription(PageConst.INPUT_DESCRIPTION)
    }
}
