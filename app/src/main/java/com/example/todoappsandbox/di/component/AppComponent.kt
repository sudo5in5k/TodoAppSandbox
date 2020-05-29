package com.example.todoappsandbox.di.component

import com.example.todoappsandbox.di.module.AppModule
import com.example.todoappsandbox.di.module.DatabaseModule
import com.example.todoappsandbox.di.module.RepositoryModule
import com.example.todoappsandbox.di.module.ViewModelModule
import com.example.todoappsandbox.ui.App
import com.example.todoappsandbox.ui.list.TodoActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        DatabaseModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {

    fun inject(app: App)
    fun inject(todoActivity: TodoActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: App): AppComponent
    }
}