package com.example.todoappsandbox.di.module

import android.content.Context
import com.example.todoappsandbox.ui.App
import dagger.Binds
import dagger.Module

@Module
interface AppModule {

    @Binds
    fun provideContext(app: App): Context
}