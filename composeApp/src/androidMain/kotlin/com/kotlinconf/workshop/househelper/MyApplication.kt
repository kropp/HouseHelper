package com.kotlinconf.workshop.househelper

import android.app.Application
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(createKoinConfig())
    }
}
