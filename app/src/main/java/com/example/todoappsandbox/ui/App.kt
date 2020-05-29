package com.example.todoappsandbox.ui

import android.app.Application
import com.example.todoappsandbox.di.component.AppComponent
import com.example.todoappsandbox.di.component.DaggerAppComponent

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.factory().create(this)
        component.inject(this)
    }

    companion object {
        lateinit var component: AppComponent
        fun getApplicationComponent() = component
    }
}