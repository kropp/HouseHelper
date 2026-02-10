package com.kotlinconf.workshop.househelper

import org.koin.core.context.startKoin

fun startKoin() {
    startKoin(createKoinConfig())
}
