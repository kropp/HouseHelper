package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import househelper.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Column {
        Image(painterResource(Res.drawable.lightbulb), contentDescription = stringResource(Res.string.lightbulb))
        Image(painterResource(Res.drawable.switch), contentDescription = stringResource(Res.string.switch))
        Image(painterResource(Res.drawable.humidity), contentDescription = stringResource(Res.string.humidity))
        Image(painterResource(Res.drawable.thermostat), contentDescription = stringResource(Res.string.thermostat))
    }
}