package com.example.amovtp.ui.screens

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
import com.example.amovtp.ui.screens.AddScreens.AddCategoryScreen
import com.example.amovtp.ui.screens.AddScreens.AddLocationScreen
import com.example.amovtp.ui.screens.AddScreens.AddPointOfInterestScreen
import com.example.amovtp.ui.viewmodels.AddViewModels.AddCategoryViewModel
import com.example.amovtp.ui.viewmodels.AddViewModels.AddLocationViewModel
import com.example.amovtp.ui.viewmodels.AddViewModels.AddPointOfInterestViewModel
import com.example.amovtp.ui.viewmodels.LocationsViewModel
import com.example.amovtp.ui.viewmodels.PointsOfInterestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    //TODO: ver repositórios e ter cuidado em declarar a viewmodel mais que uma vez
    var locationsViewModel = LocationsViewModel()
    var pointsOfInterestViewModel = PointsOfInterestViewModel()
    var addLocationViewModel = AddLocationViewModel()
    var addPointOfInterestViewModel = AddPointOfInterestViewModel()
    var addCategoryViewModel = AddCategoryViewModel()

    var addInfo by remember { mutableStateOf(false) }

    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        addInfo =
            (destination.route == Screens.LOCATIONS.route || destination.route == Screens.POINTS_OF_INTEREST.route)
    }

    Scaffold(

        topBar = {

            if (currentScreen != null && Screens.valueOf(currentScreen!!.destination.route!!) != Screens.LOGIN)
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
                                if(Screens.valueOf(currentScreen!!.destination.route!!) == Screens.LOCATIONS)
                                    navController?.navigate(Screens.ADD_LOCATION.route)
                                else if (Screens.valueOf(currentScreen!!.destination.route!!) == Screens.POINTS_OF_INTEREST){
                                    //TODO: fazer um dropdown button para escolher entre adicionar uma categoria ou um ponto de interesse
                                }
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
                LoginScreen(navController, Screens.REGISTER, Screens.LOCATIONS)
            }

            composable(Screens.REGISTER.route) {
                RegisterScreen(navController, Screens.LOGIN)
            }

            composable(Screens.LOCATIONS.route) {
                LocationsScreen(navController, locationsViewModel!!) {}
            }

            composable(Screens.POINTS_OF_INTEREST.route) {
                PointsOfInterestScreen(navController, pointsOfInterestViewModel!!) {}
            }

            composable(Screens.ADD_LOCATION.route){
                AddLocationScreen(navController, addLocationViewModel!!)
            }

            composable(Screens.ADD_POINT_OF_INTEREST.route){
                AddPointOfInterestScreen(navController, addPointOfInterestViewModel!!)
            }

            composable(Screens.ADD_CATEGORY.route){
                AddCategoryScreen(navController, addCategoryViewModel!!)
            }

        }

    }

}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}