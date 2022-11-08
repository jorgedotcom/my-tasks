package com.jorgedotcom.mytasks

import android.app.Application
import com.jorgedotcom.mytasks.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TasksApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TasksApplication)
            modules(appModule)
        }
    }
}