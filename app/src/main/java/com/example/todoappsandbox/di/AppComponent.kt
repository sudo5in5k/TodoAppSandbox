package com.example.todoappsandbox.di

import com.example.todoappsandbox.ui.App
import com.example.todoappsandbox.ui.list.TodoActivity
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