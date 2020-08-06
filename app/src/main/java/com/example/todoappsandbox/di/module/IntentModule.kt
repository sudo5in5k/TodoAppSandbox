package com.example.todoappsandbox.di.module

import androidx.annotation.Nullable
import com.example.todoappsandbox.data.repository.db.TodoEntity
import com.example.todoappsandbox.ui.create.NewTodoActivity
import com.example.todoappsandbox.utils.Consts
import dagger.Module
import dagger.Provides

@Module
object IntentModule {
    @Nullable
    @Provides
    fun provideEntity(activity: NewTodoActivity): TodoEntity? =
        activity.intent.getParcelableExtra(Consts.INTENT) as? TodoEntity
}