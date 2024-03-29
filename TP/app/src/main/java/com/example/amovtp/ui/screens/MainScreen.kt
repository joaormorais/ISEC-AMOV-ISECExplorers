package com.example.amovtp.ui.screens

import CreditsScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.amovtp.MyApplication
import com.example.amovtp.R
import com.example.amovtp.ui.screens.addScreens.AddCategoryScreen
import com.example.amovtp.ui.screens.addScreens.AddLocationScreen
import com.example.amovtp.ui.screens.addScreens.AddPointOfInterestScreen
import com.example.amovtp.ui.screens.editScreens.EditCategoryScreen
import com.example.amovtp.ui.screens.editScreens.EditLocationScreen
import com.example.amovtp.ui.screens.editScreens.EditPointOfInterestScreen
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
import com.example.amovtp.ui.viewmodels.editViewModels.EditCategoryViewModel
import com.example.amovtp.ui.viewmodels.editViewModels.EditCategoryViewModelFactory
import com.example.amovtp.ui.viewmodels.editViewModels.EditLocationViewModel
import com.example.amovtp.ui.viewmodels.editViewModels.EditLocationViewModelFactory
import com.example.amovtp.ui.viewmodels.editViewModels.EditPointOfInterestViewModel
import com.example.amovtp.ui.viewmodels.editViewModels.EditPointOfInterestViewModelFactory
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
    var editLocationViewModel: EditLocationViewModel?
    var editPointOfInterestViewModel: EditPointOfInterestViewModel?
    var editCategoryViewModel: EditCategoryViewModel?

    var isLogin by remember { mutableStateOf(false) }
    var isLocation by remember { mutableStateOf(false) }
    var addLocation by remember { mutableStateOf(false) }
    var addPointOfInterest by remember { mutableStateOf(false) }
    var addCategory by remember { mutableStateOf(false) }
    var screenTitle by remember { mutableStateOf("") }

    val loginTitle = stringResource(R.string.title_login)
    val locationTitle = stringResource(R.string.title_locations)
    val pointOfInterestTitle = stringResource(R.string.title_points_of_interest)
    val categoriesTitle = stringResource(R.string.title_categories)
    val addLocationTitle = stringResource(R.string.title_add_location)
    val addPointOfInterestTitle = stringResource(R.string.title_add_point_of_interest)
    val addCategoryTitle = stringResource(R.string.title_add_category)
    val editLocationTitle = stringResource(R.string.title_edit_location)
    val editPointOfInterestTitle = stringResource(R.string.title_edit_point_of_interest)
    val editCategoryTitle = stringResource(R.string.title_edit_category)
    val creditsTitle = stringResource(R.string.title_credits)

    navController.addOnDestinationChangedListener { controller, destination, arguments ->

        isLogin = (destination.route == Screens.LOGIN.route)
        isLocation = (destination.route == Screens.LOCATIONS.route)
        addLocation = (destination.route == Screens.LOCATIONS.route)
        addPointOfInterest = (destination.route == Screens.POINTS_OF_INTEREST.route)
        addCategory = (destination.route == Screens.CATEGORIES.route)

        when (destination.route) {
            Screens.LOGIN.route -> {
                screenTitle = loginTitle
            }

            Screens.LOCATIONS.route -> {
                screenTitle = locationTitle
            }

            Screens.POINTS_OF_INTEREST.route -> {
                screenTitle = pointOfInterestTitle
            }

            Screens.CATEGORIES.route -> {
                screenTitle = categoriesTitle
            }

            Screens.ADD_LOCATION.route -> {
                screenTitle = addLocationTitle
            }

            Screens.ADD_POINT_OF_INTEREST.route -> {
                screenTitle = addPointOfInterestTitle
            }

            Screens.ADD_CATEGORY.route -> {
                screenTitle = addCategoryTitle
            }

            Screens.EDIT_LOCATIONS.route -> {
                screenTitle = editLocationTitle
            }

            Screens.EDIT_POINT_OF_INTEREST.route -> {
                screenTitle = editPointOfInterestTitle
            }

            Screens.EDIT_CATEGORY.route -> {
                screenTitle = editCategoryTitle
            }

            Screens.CREDITS.route -> {
                screenTitle = creditsTitle
            }

            else -> {
                screenTitle = destination.route.toString()
            }
        }
    }

    AMOVTPTheme {
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
                                IconButton(
                                    onClick = {
                                        if (isLocation) {
                                            if (app.userData.localUser.value.userId != "")
                                                app.userData.signOut()
                                            navController.navigateUp()
                                        } else {
                                            navController.navigateUp()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isLocation) Icons.Filled.ExitToApp else Icons.Filled.ArrowBack,
                                        contentDescription = if (isLocation) "Logout" else "Back"
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
                        RegisterScreen(
                            registerViewModel!!,
                            navController
                        )
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

                        val itemId = it.arguments?.getString("itemName")
                        pointsOfInterestViewModel =
                            viewModel(
                                factory = PointsOfInterestViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        PointsOfInterestScreen(
                            pointsOfInterestViewModel!!,
                            itemId!!,
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
                        CategoriesScreen(categoriesViewModel!!, navController)
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
                            viewModel(
                                factory = AddCategoryViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        AddCategoryScreen(addCategoryViewModel!!, navController)
                    }

                    composable(Screens.EDIT_LOCATIONS.route,
                        arguments = listOf(
                            navArgument("itemId") {
                                type = NavType.StringType
                                defaultValue = Consts.DEFAULT_VALUE
                                nullable = false
                            }
                        )
                    ) {

                        val itemId = it.arguments?.getString("itemId")
                        editLocationViewModel =
                            viewModel(
                                factory = EditLocationViewModelFactory(
                                    app.geoData, app.userData
                                )
                            )
                        EditLocationScreen(
                            editLocationViewModel!!,
                            itemId!!,
                            navController
                        )
                    }

                    composable(Screens.EDIT_POINT_OF_INTEREST.route,
                        arguments = listOf(
                            navArgument("itemId") {
                                type = NavType.StringType
                                defaultValue = Consts.DEFAULT_VALUE
                                nullable = false
                            }
                        )
                    ) {

                        val itemId = it.arguments?.getString("itemId")
                        editPointOfInterestViewModel =
                            viewModel(
                                factory = EditPointOfInterestViewModelFactory(
                                    app.geoData,
                                    app.userData
                                )
                            )
                        EditPointOfInterestScreen(
                            editPointOfInterestViewModel!!,
                            itemId!!,
                            navController
                        )
                    }

                    composable(Screens.EDIT_CATEGORY.route,
                        arguments = listOf(
                            navArgument("itemId") {
                                type = NavType.StringType
                                defaultValue = Consts.DEFAULT_VALUE
                                nullable = false
                            }
                        )
                    ) {

                        val itemId = it.arguments?.getString("itemId")
                        editCategoryViewModel =
                            viewModel(
                                factory = EditCategoryViewModelFactory(
                                    app.geoData, app.userData
                                )
                            )
                        EditCategoryScreen(
                            editCategoryViewModel!!,
                            itemId!!,
                            navController
                        )
                    }

                    composable(Screens.CREDITS.route) {
                        CreditsScreen()
                    }

                }

            }
        }
    }

}