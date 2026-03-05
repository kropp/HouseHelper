package com.kotlinconf.workshop.househelper

import android.app.Application
import com.kotlinconf.workshop.househelper.di.AppGraph
import dev.zacsweers.metro.createGraph

class MyApplication : Application() {
    val appGraph: AppGraph by lazy {
        createGraph<AppGraph>()
    }
}
