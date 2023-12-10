package com.example.amovtp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.amovtp.MyApplication
import com.example.amovtp.R
import com.example.amovtp.ui.screens.addScreens.AddCategoryScreen
import com.example.amovtp.ui.screens.addScreens.AddLocationScreen
import com.example.amovtp.ui.screens.addScreens.AddPointOfInterestScreen
import com.example.amovtp.ui.screens.infoScreens.LocationsScreen
import com.example.amovtp.ui.screens.infoScreens.PointsOfInterestScreen
import com.example.amovtp.ui.screens.usersScreens.LoginScreen
import com.example.amovtp.ui.screens.usersScreens.RegisterScreen
import com.example.amovtp.ui.viewmodels.addViewModels.AddCategoryViewModel
import com.example.amovtp.ui.viewmodels.addViewModels.AddCategoryViewModelFactory
import com.example.amovtp.ui.viewmodels.addViewModels.AddLocationViewModel
import com.example.amovtp.ui.viewmodels.addViewModels.AddLocationViewModelFactory
import com.example.amovtp.ui.viewmodels.addViewModels.AddPointOfInterestViewModel
import com.example.amovtp.ui.viewmodels.addViewModels.AddPointOfInterestViewModelFactory
import com.example.amovtp.ui.viewmodels.infoViewModels.LocationsViewModel
import com.example.amovtp.ui.viewmodels.infoViewModels.LocationsViewModelFactory
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModelFactory
import com.example.amovtp.ui.viewmodels.usersViewModels.LoginViewModel
import com.example.amovtp.ui.viewmodels.usersViewModels.LoginViewModelFactory
import com.example.amovtp.ui.viewmodels.usersViewModels.RegisterViewModel
import com.example.amovtp.ui.viewmodels.usersViewModels.RegisterViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {

    val context = LocalContext.current
    val app = context.applicationContext as MyApplication
    val defaultString = stringResource(R.string.defaultvalue)

    var isExpanded by remember { mutableStateOf(false) }
    val addCategoryString = stringResource(R.string.add_category)
    val addPointString = stringResource(R.string.add_point)
    val items = listOf(addCategoryString, addPointString)
    var selectedIndex by remember { mutableStateOf(0) }

    var loginViewModel: LoginViewModel? = null
    var registerViewModel: RegisterViewModel? = null
    var locationsViewModel: LocationsViewModel? = null
    var pointsOfInterestViewModel: PointsOfInterestViewModel? = null
    var addLocationViewModel: AddLocationViewModel? = null
    var addPointOfInterestViewModel: AddPointOfInterestViewModel? = null
    var addCategoryViewModel: AddCategoryViewModel? = null

    var addLoc by remember { mutableStateOf(false) }
    var addPointOrCategory by remember { mutableStateOf(false) }

    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        addLoc = (destination.route == Screens.LOCATIONS.route)
        addPointOrCategory = (destination.route == Screens.POINTS_OF_INTEREST.route)
    }

    Scaffold(

        topBar = {

            //if (currentScreen != null && Screens.valueOf(currentScreen!!.destination.route!!) != Screens.LOGIN)
            if (currentScreen != null && currentScreen.toString() != Screens.LOGIN.route)
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

                        if (addLoc) {
                            IconButton(onClick = {
                                navController.navigate(Screens.ADD_LOCATION.route)
                            }) {
                                Icon(
                                    Icons.Filled.AddCircle,
                                    contentDescription = "Add Information"
                                )
                            }
                        } else if (addPointOrCategory) {
                            IconButton(onClick = {
                                isExpanded = true
                            }) {
                                Icon(
                                    Icons.Filled.AddCircle,
                                    contentDescription = "Add Information"
                                )
                            }

                            DropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                modifier = Modifier
                                    .wrapContentWidth()
                            ) {

                                items.forEachIndexed { index, s ->
                                    DropdownMenuItem(
                                        text = { Text(text = s) },
                                        onClick = {
                                            selectedIndex = index
                                            isExpanded = false

                                            when (s) {

                                                addCategoryString -> {
                                                    navController.navigate(Screens.ADD_CATEGORY.route)
                                                }

                                                addPointString -> {
                                                    navController.navigate(Screens.ADD_POINT_OF_INTEREST.route)
                                                }

                                            }

                                        }
                                    )
                                }

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
                loginViewModel = viewModel(factory = LoginViewModelFactory(app.usersData))
                LoginScreen(loginViewModel!!, navController, Screens.REGISTER, Screens.LOCATIONS)
            }

            composable(Screens.REGISTER.route) {
                registerViewModel = viewModel(factory = RegisterViewModelFactory(app.usersData))
                RegisterScreen(registerViewModel!!, navController, Screens.LOGIN)
            }

            composable(Screens.LOCATIONS.route) {
                locationsViewModel =
                    viewModel(factory = LocationsViewModelFactory(app.geoData, app.usersData))
                LocationsScreen(locationsViewModel!!, navController)
            }

            composable(Screens.POINTS_OF_INTEREST.route,
                arguments = listOf(
                    navArgument("itemName") {
                        type = NavType.StringType
                        defaultValue = defaultString
                        nullable = false
                    }
                )
            ) {

                val itemName = it.arguments?.getString("itemName")
                pointsOfInterestViewModel =
                    viewModel(
                        factory = PointsOfInterestViewModelFactory(
                            app.geoData,
                            app.usersData
                        )
                    )
                PointsOfInterestScreen(pointsOfInterestViewModel!!, itemName!!)
            }

            composable(Screens.ADD_LOCATION.route) {
                addLocationViewModel = viewModel(factory = AddLocationViewModelFactory(app.geoData))
                AddLocationScreen(addLocationViewModel!!, navController)
            }

            composable(Screens.ADD_POINT_OF_INTEREST.route) {
                addPointOfInterestViewModel =
                    viewModel(factory = AddPointOfInterestViewModelFactory(app.geoData))
                AddPointOfInterestScreen(addPointOfInterestViewModel!!, navController)
            }

            composable(Screens.ADD_CATEGORY.route) {
                addCategoryViewModel = viewModel(factory = AddCategoryViewModelFactory(app.geoData))
                AddCategoryScreen(addCategoryViewModel!!, navController)
            }

        }

    }

}