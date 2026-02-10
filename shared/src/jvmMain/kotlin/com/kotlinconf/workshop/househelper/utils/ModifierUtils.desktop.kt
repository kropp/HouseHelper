package com.kotlinconf.workshop.househelper.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.onClick
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerType

@OptIn(ExperimentalFoundationApi::class)
actual fun Modifier.onRightClick(onClick: () -> Unit): Modifier =
    onClick(
        matcher = PointerMatcher.pointer(PointerType.Mouse, PointerButton.Secondary),
        onClick = onClick,
    )
