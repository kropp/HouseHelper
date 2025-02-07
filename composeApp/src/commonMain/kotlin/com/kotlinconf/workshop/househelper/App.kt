package com.kotlinconf.workshop.househelper

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.kotlinconf.workshop.househelper.navigation.*
import househelper.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = StartScreens) {
        navigation<StartScreens>(startDestination = Welcome) {
            composable<Welcome> {
                Onboarding(Res.string.onboarding_welcome, { navController.navigate(Onboarding) })
            }
            composable<Onboarding> {
                Onboarding(Res.string.onboarding_about, { navController.navigate(OnboardingDone) })
            }
            composable<OnboardingDone> {
                Onboarding(Res.string.onboarding_done, { navController.navigate(Dashboard) })
            }
        }
        composable<Dashboard> {
            Column {
                Image(painterResource(Res.drawable.lightbulb), contentDescription = stringResource(Res.string.lightbulb))
                Image(painterResource(Res.drawable.switch), contentDescription = stringResource(Res.string.switch))
                Image(painterResource(Res.drawable.humidity), contentDescription = stringResource(Res.string.humidity))
                Image(painterResource(Res.drawable.thermostat), contentDescription = stringResource(Res.string.thermostat))
            }
        }
    }
}