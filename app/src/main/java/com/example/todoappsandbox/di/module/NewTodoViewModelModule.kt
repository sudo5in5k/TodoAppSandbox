package com.example.todoappsandbox.di.module

import androidx.lifecycle.ViewModel
import com.example.todoappsandbox.di.ViewModelKey
import com.example.todoappsandbox.ui.create.NewTodoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NewTodoViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewTodoViewModel::class)
    abstract fun bindNewTodoViewModel(viewModel: NewTodoViewModel): ViewModel
}