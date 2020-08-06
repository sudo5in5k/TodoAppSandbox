package com.example.todoappsandbox.di.module

import com.example.todoappsandbox.di.ActivityScope
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.ui.list.TodoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [MainModule::class, TodoViewModelModule::class])
    @ActivityScope
    abstract fun bindTodoActivity(): TodoActivity

    @ContributesAndroidInjector(modules = [IntentModule::class, NewTodoViewModelModule::class])
    @ActivityScope
    abstract fun bindNewTodoActivity(): NewTodoActivity
}