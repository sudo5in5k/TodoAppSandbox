package com.example.todoappsandbox.di.module

import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.di.ViewModelKey
import com.example.todoappsandbox.ui.list.TodoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(TodoViewModel::class)
    abstract fun bindTodoViewModel(viewModel: TodoViewModel): ViewModel
}