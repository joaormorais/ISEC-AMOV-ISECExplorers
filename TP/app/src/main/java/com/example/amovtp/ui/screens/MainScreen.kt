package com.example.amovtp.ui.screens

import androidx.compose.foundation.layout.Box
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.amovtp.MyApplication
import com.example.amovtp.ui.screens.addScreens.AddCategoryScreen
import com.example.amovtp.ui.screens.addScreens.AddLocationScreen
import com.example.amovtp.ui.screens.addScreens.AddPointOfInterestScreen
import com.example.amovtp.ui.screens.infoScreens.CategoriesScreen
import com.example.amovtp.ui.screens.infoScreens.LocationsScreen
import com.example.amovtp.ui.screens.infoScreens.PointsOfInterestScreen
import com.example.amovtp.ui.screens.usersScreens.LoginScreen
import com.example.amovtp.ui.screens.usersScreens.RegisterScreen
import com.example.amovtp.ui.theme.AMOVTPTheme
import com.example.amovtp.ui.viewmodels.addViewModels.AddCategoryViewModel
import com.example.amovtp.ui.viewmodels.addViewModels.AddCategoryViewModelFactory
import com.example.amovtp.ui.viewmodels.addViewModels.AddLocationViewModel
import com.example.amovtp.ui.viewmodels.addViewModels.AddLocationViewModelFactory
import com.example.amovtp.ui.viewmodels.addViewModels.AddPointOfInterestViewModel
import com.example.amovtp.ui.viewmodels.addViewModels.AddPointOfInterestViewModelFactory
import com.example.amovtp.ui.viewmodels.infoViewModels.CategoriesViewModel
import com.example.amovtp.ui.viewmodels.infoViewModels.CategoriesViewModelFactory
import com.example.amovtp.ui.viewmodels.infoViewModels.LocationsViewModel
import com.example.amovtp.ui.viewmodels.infoViewModels.LocationsViewModelFactory
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModelFactory
import com.example.amovtp.ui.viewmodels.usersViewModels.LoginViewModel
import com.example.amovtp.ui.viewmodels.usersViewModels.LoginViewModelFactory
import com.example.amovtp.ui.viewmodels.usersViewModels.RegisterViewModel
import com.example.amovtp.ui.viewmodels.usersViewModels.RegisterViewModelFactory
import com.example.amovtp.utils.Consts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val app = context.applicationContext as MyApplication

    var loginViewModel: LoginViewModel?
    var registerViewModel: RegisterViewModel?
    var locationsViewModel: LocationsViewModel?
    var pointsOfInterestViewModel: PointsOfInterestViewModel?
    var categoriesViewModel: CategoriesViewModel?
    var addLocationViewModel: AddLocationViewModel?
    var addPointOfInterestViewModel: AddPointOfInterestViewModel?
    var addCategoryViewModel: AddCategoryViewModel?

    var isLogin by remember { mutableStateOf(false) }
    var addLocation by remember { mutableStateOf(false) }
    var addPointOfInterest by remember { mutableStateOf(false) }
    var addCategory by remember { mutableStateOf(false) }
    var screenTitle by remember { mutableStateOf("") }

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        isLogin = (destination.route == Screens.LOGIN.route)
        addLocation = (destination.route == Screens.LOCATIONS.route)
        addPointOfInterest = (destination.route == Screens.POINTS_OF_INTEREST.route)
        addCategory = (destination.route == Screens.CATEGORIES.route)

        screenTitle = if(destination.route == Screens.POINTS_OF_INTEREST.route) Consts.POINTS_OF_INTEREST else destination.route.toString()
    }

    AMOVTPTheme() {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    if (!isLogin)
                        TopAppBar(
                            title = {
                                Text(screenTitle, color = Color.White)
                            },

                            navigationIcon = {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        Icons.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            },

                            actions = {

                                if (addLocation) {
                                    IconButton(onClick = {
                                        navController.navigate(Screens.ADD_LOCATION.route)
                                    }) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription = "Add Information"
                                        )
                                    }
                                } else if (addPointOfInterest) {
                                    IconButton(onClick = {
                                        navController.navigate(Screens.ADD_POINT_OF_INTEREST.route)
                                    }) {
                                        Icon(
                                            Icons.Filled.AddCircle,
                                            contentDescription = "Add Information"
                                        )
                                    }
                                } else if (addCategory) {
                                    IconButton(onClick = {
                                        navController.navigate(Screens.ADD_CATEGORY.route)
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
                modifier = modifier.fillMaxSize()
            ) {

                NavHost(
                    navController = navController,
                    startDestination = Screens.LOGIN.route,
                    modifier = modifier
                        .padding(it)
                ) {

                    composable(Screens.LOGIN.route) {
                        loginViewModel = viewModel(factory = LoginViewModelFactory(app.userData))
                        LoginScreen(
                            loginViewModel!!,
                            navController,
                            Screens.REGISTER,
                            Screens.LOCATIONS
                        )
                    }

                    composable(Screens.REGISTER.route) {
                        registerViewModel =
                            viewModel(factory = RegisterViewModelFactory(app.userData))
                        RegisterScreen(registerViewModel!!, navController, Screens.LOGIN)
                    }

                    composable(Screens.LOCATIONS.route) {
                        locationsViewModel =
                            viewModel(
                                factory = LocationsViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        LocationsScreen(locationsViewModel!!, navController)
                    }

                    composable(Screens.POINTS_OF_INTEREST.route,
                        arguments = listOf(
                            navArgument("itemName") {
                                type = NavType.StringType
                                defaultValue = Consts.DEFAULT_VALUE
                                nullable = false
                            }
                        )
                    ) {

                        val itemName = it.arguments?.getString("itemName")
                        pointsOfInterestViewModel =
                            viewModel(
                                factory = PointsOfInterestViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        PointsOfInterestScreen(
                            pointsOfInterestViewModel!!,
                            itemName!!,
                            navController
                        )
                    }

                    composable(Screens.CATEGORIES.route) {
                        categoriesViewModel =
                            viewModel(
                                factory = CategoriesViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        CategoriesScreen(categoriesViewModel!!)
                    }

                    composable(Screens.ADD_LOCATION.route) {
                        addLocationViewModel =
                            viewModel(
                                factory = AddLocationViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        AddLocationScreen(addLocationViewModel!!, navController)
                    }

                    composable(Screens.ADD_POINT_OF_INTEREST.route) {
                        addPointOfInterestViewModel =
                            viewModel(
                                factory = AddPointOfInterestViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        AddPointOfInterestScreen(addPointOfInterestViewModel!!, navController)
                    }

                    composable(Screens.ADD_CATEGORY.route) {
                        addCategoryViewModel =
                            viewModel(factory = AddCategoryViewModelFactory(app.geoData))
                        AddCategoryScreen(addCategoryViewModel!!, navController)
                    }

                }

            }
        }
    }

}