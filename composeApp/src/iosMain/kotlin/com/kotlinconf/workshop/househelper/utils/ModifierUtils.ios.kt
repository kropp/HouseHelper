package com.kotlinconf.workshop.househelper.utils

import androidx.compose.ui.Modifier

actual fun Modifier.onRightClick(onClick: () -> Unit): Modifier = this
