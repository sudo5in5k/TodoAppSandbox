package com.example.todoappsandbox.di.component

import com.example.todoappsandbox.di.module.AppModule
import com.example.todoappsandbox.di.module.DatabaseModule
import com.example.todoappsandbox.di.module.MainModule
import com.example.todoappsandbox.di.module.TodoActivityBuilder
import com.example.todoappsandbox.ui.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        TodoActivityBuilder::class,
        DatabaseModule::class,
        MainModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: App): AppComponent
    }
}