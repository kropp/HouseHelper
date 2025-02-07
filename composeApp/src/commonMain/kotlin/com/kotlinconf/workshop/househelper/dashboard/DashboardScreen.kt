package com.kotlinconf.workshop.househelper.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.househelper.Appliance
import com.kotlinconf.workshop.househelper.HumidityAppliance
import com.kotlinconf.workshop.househelper.LightAppliance
import com.kotlinconf.workshop.househelper.Room
import com.kotlinconf.workshop.househelper.SwitchAppliance
import com.kotlinconf.workshop.househelper.ThermostatAppliance
import com.kotlinconf.workshop.househelper.Toggleable
import househelper.composeapp.generated.resources.Res
import househelper.composeapp.generated.resources.humidity
import househelper.composeapp.generated.resources.lightbulb
import househelper.composeapp.generated.resources.switch
import househelper.composeapp.generated.resources.thermostat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val rooms by viewModel.rooms.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(rooms) { room ->
                RoomSection(room)
            }
        }
    }
}

@Composable
private fun RoomSection(room: Room) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(room.appliances) { appliance ->
                    ApplianceCard(appliance = appliance, room = room)
                }
            }
        }
    }
}

@Composable
private fun ApplianceCard(
    appliance: Appliance,
    room: Room,
    viewModel: DashboardViewModel = koinViewModel()
) {
    Box {
        Card(
            modifier = Modifier.size(100.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            onClick = { viewModel.onApplianceClicked(room, appliance) }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(appliance.iconResource),
                        contentDescription = appliance.name,
                        modifier = Modifier.size(40.dp),
                        colorFilter = if (appliance is Toggleable && appliance.isOn) {
                            ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        } else null
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = appliance.name,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
