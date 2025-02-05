package com.kotlinconf.workshop.househelper.utils

import androidx.compose.ui.Modifier

expect fun Modifier.onRightClick(onClick: () -> Unit): Modifier
