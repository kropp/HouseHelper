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

@Composable
fun RenameDeviceScreen(
    deviceId: DeviceId,
    currentName: String,
    onDismiss: () -> Unit,
    viewModel: RenameDeviceViewModel = koinViewModel(),
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = currentName,
                selection = TextRange(currentName.length)
            )
        )
    }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val renamePerformed by viewModel.renamePerformed.collectAsStateWithLifecycle()
    LaunchedEffect(renamePerformed) {
        onDismiss()
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Rename Device",
            style = MaterialTheme.typography.headlineSmall
        )

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
                    viewModel.renameDevice(deviceId, textFieldValue.text)
                    onDismiss()
                }
            ) {
                Text("Rename")
            }
        }
    }
}
