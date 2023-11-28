package com.example.amovtp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    var locationsViewModel: LocationsViewModel? = null
    var pointsOfInterestViewModel: PointsOfInterestViewModel? = null

    var addInfo by remember { mutableStateOf(false) }

    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        addInfo = (destination.route == Screens.LOCATIONS.route)
    }

    Scaffold(

        //TODO: fazer as condições para alternar a topbar consoante o menu em que estamos (login)
        topBar = {

            if (currentScreen != null && Screens.valueOf(currentScreen!!.destination.route!!) != Screens.LOGIN) // !! para q?
                TopAppBar(
                    title = {},

                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },

                    actions = {
                        if (addInfo) {
                            IconButton(onClick = {

                            }) {
                                Icon(
                                    Icons.Filled.AddCircle,
                                    contentDescription = "Add Information"
                                )
                            }
                        }
                    },

                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.inversePrimary,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                )
        },
        modifier = Modifier.fillMaxSize()
    ) {

        NavHost(
            navController = navController,
            startDestination = Screens.LOGIN.route,
            modifier = Modifier
                .padding(it)
        ) {

            composable(Screens.LOGIN.route) {
                Login(navController, Screens.REGISTER, Screens.LOCATIONS)
            }

            composable(Screens.REGISTER.route) {
                Register(navController, Screens.LOGIN)
            }

            composable(Screens.LOCATIONS.route) {
                //TODO: ver repositórios e ter cuidado em declarar a viewmodel mais que uma vez
                locationsViewModel = LocationsViewModel()
                Locations(navController, locationsViewModel!!) {}
            }

            composable(Screens.POINTS_OF_INTEREST.route) {
                pointsOfInterestViewModel = PointsOfInterestViewModel()
                PointsOfInterest(navController, pointsOfInterestViewModel!!) {}
            }

        }

    }

}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}