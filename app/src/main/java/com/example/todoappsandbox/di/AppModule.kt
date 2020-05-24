package com.example.todoappsandbox.di

import android.content.Context
import com.example.todoappsandbox.ui.App
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun provideContext(app: App): Context
}