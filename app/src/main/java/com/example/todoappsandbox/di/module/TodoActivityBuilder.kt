package com.example.todoappsandbox.di.module

import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.di.ActivityScope
import com.example.todoappsandbox.di.ViewModelKey
import com.example.todoappsandbox.ui.list.TodoActivity
import com.example.todoappsandbox.ui.list.TodoViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TodoActivityBuilder {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun bindTodoActivity(): TodoActivity

    @Binds
    @IntoMap
    @ViewModelKey(TodoViewModel::class)
    @ActivityScope
    abstract fun bindTodoViewModel(viewModel: TodoViewModel): ViewModel
}