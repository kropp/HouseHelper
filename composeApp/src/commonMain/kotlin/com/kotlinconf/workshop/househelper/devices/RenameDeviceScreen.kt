package com.kotlinconf.workshop.househelper.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlinconf.workshop.househelper.DeviceId
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RenameDeviceScreen(
    deviceId: DeviceId,
    onDismiss: () -> Unit,
    viewModel: RenameDeviceViewModel = koinViewModel { parametersOf(deviceId) },
) {
    val renamePerformed by viewModel.renamePerformed.collectAsStateWithLifecycle()
    LaunchedEffect(renamePerformed) {
        if (renamePerformed) {
            onDismiss()
        }
    }

    val currentName by viewModel.deviceName.collectAsStateWithLifecycle()
    var textFieldValue by remember(currentName) {
        val initialName = currentName ?: ""
        mutableStateOf(
            TextFieldValue(
                text = initialName,
                selection = TextRange(initialName.length)
            )
        )
    }

    // TODO Task 10: wrap this Column with a background
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Rename Device",
            style = MaterialTheme.typography.headlineSmall
        )

        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.focusRequester(focusRequester)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
            TextButton(
                onClick = {
                    viewModel.renameDevice(textFieldValue.text)
                    onDismiss()
                }
            ) {
                Text("Rename")
            }
        }
    }
}
