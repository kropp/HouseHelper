package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.onboarding_next_button
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Onboarding(
    text: StringResource,
    onNext: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(text))
        Button(onClick = onNext) {
            Text(stringResource(Res.string.onboarding_next_button))
        }
    }
}