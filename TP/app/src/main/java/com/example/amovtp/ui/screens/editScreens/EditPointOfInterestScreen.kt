package com.example.amovtp.ui.screens.editScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.amovtp.R
import com.example.amovtp.data.Category
import com.example.amovtp.data.Location
import com.example.amovtp.ui.composables.AddInfoComposables.GeoDescription
import com.example.amovtp.ui.composables.AddInfoComposables.NameDescription
import com.example.amovtp.ui.viewmodels.editViewModels.EditPointOfInterestViewModel
import com.example.amovtp.utils.Consts

@Composable
fun EditPointOfInterestScreen(
    editPointOfInterestViewModel: EditPointOfInterestViewModel,
    itemId: String,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    val currentPointOfInterest = editPointOfInterestViewModel.getCurrentEditingPointOfInterest(itemId)
    var name by remember { mutableStateOf(currentPointOfInterest?.name ?: "") }
    var description by remember { mutableStateOf(currentPointOfInterest?.description ?: "") }
    var lat by remember { mutableStateOf(currentPointOfInterest?.lat) }
    var long by remember { mutableStateOf(currentPointOfInterest?.long) }
    var isManual by remember { mutableStateOf(currentPointOfInterest?.isManualCoords) }

    val selectedLocations by remember { mutableStateOf(currentPointOfInterest?.locations) }
    var expanded1 by remember { mutableStateOf(false) }
    val locationList: MutableState<List<Location>> = editPointOfInterestViewModel.getLocations()
    var selectedCategory by remember { mutableStateOf(currentPointOfInterest?.category) }
    var expanded2 by remember { mutableStateOf(false) }
    val categoryList: MutableState<List<Category>> = editPointOfInterestViewModel.getCategories()

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val unkownError = stringResource(R.string.unknown_error)
    val fillNameError = stringResource(R.string.invalid_name)
    val fillDescriptionError = stringResource(R.string.invalid_description)
    val fillCoordinatesError = stringResource(R.string.invalid_coordinates)
    val fillLocationError = stringResource(R.string.invalid_location)
    val fillCategoryError = stringResource(R.string.invalid_category)
    val nameExistsError = stringResource(R.string.error_existing_name)
    val locationExistsError = stringResource(R.string.error_existing_location)
    val loginNeededError = stringResource(R.string.error_you_have_to_login)

    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            snackbarHostState.showSnackbar(errorMessage ?: unkownError)
            showSnackBar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier=modifier.padding(top = 32.dp)
    ){innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item{

                NameDescription(
                    name,
                    description,
                    nameChanged = { newName ->
                        name = newName
                    },
                    descriptionChanged = { newDescription ->
                        description = newDescription
                    }
                )
                Spacer(modifier = modifier.height(8.dp))
                GeoDescription(
                    lat.toString(),
                    long.toString(),
                    isManual!!,
                    latChanged = { newLat ->
                        lat = newLat
                    },
                    longChanged = { newLong ->
                        long = newLong
                    },
                    manualChanged = { newManual ->
                        isManual = newManual
                        if (!newManual) {
                            val tempLocation = editPointOfInterestViewModel.getCurrentLocation()
                            lat = tempLocation.value!!.latitude
                            long = tempLocation.value!!.longitude
                        }
                    }
                )
                Spacer(modifier = modifier.height(8.dp))
                Row {
                    Box {
                        Button(
                            onClick = {
                                expanded1 = true
                            },
                            modifier = modifier
                                .wrapContentWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(stringResource(R.string.choose_locations))
                                Icon(Icons.Rounded.KeyboardArrowDown, "down")
                            }
                        }

                        DropdownMenu(
                            expanded = expanded1,
                            onDismissRequest = { expanded1 = false },
                            modifier = modifier
                                .wrapContentWidth()
                                .heightIn(max = 200.dp)
                                .wrapContentHeight(Alignment.Top)
                        ) {
                            locationList.value.forEach { location ->

                                //TODO: terminar + pintar o fundo
                                var isLocationPicked by remember { mutableStateOf(selectedLocations?.contains(location.name)) }

                                DropdownMenuItem(
                                    text = {
                                        if (isLocationPicked!!) Text(
                                            location.name,
                                            color = Color.Green
                                        ) else Text(location.name)
                                    },
                                    onClick = {
                                        if (!selectedLocations!!.removeAll { it == location.name }) {
                                            selectedLocations!!.add(location.name)
                                            isLocationPicked = true
                                        } else {
                                            selectedLocations!!.remove(location.name)
                                            isLocationPicked = false
                                        }
                                    })
                            }
                        }
                    }
                    Box {
                        Button(
                            onClick = {
                                expanded2 = true
                            },
                            modifier = modifier
                                .wrapContentWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(if (selectedCategory!!.isEmpty()) stringResource(R.string.choose_a_category) else selectedCategory!!)
                                Icon(Icons.Rounded.KeyboardArrowDown, "down")
                            }
                        }
                        DropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false },
                            modifier = modifier
                                .wrapContentWidth()
                                .heightIn(max = 200.dp)
                                .wrapContentHeight(Alignment.Top)
                        ) {
                            categoryList.value.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategory = category.name
                                        expanded2 = false
                                    })
                            }
                        }
                    }
                }

                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Consts.CONFIRMATION_COLOR, contentColor = Color.Black),
                    onClick = {
                        val validationResult = editPointOfInterestViewModel.isEditPointOfInterestValid(
                            name,
                            description,
                            lat,
                            long,
                            selectedLocations!!,
                            selectedCategory!!,
                            fillNameError,
                            fillDescriptionError,
                            fillCoordinatesError,
                            fillLocationError,
                            fillCategoryError
                        ) { msg ->
                            errorMessage=msg
                        }

                        if(validationResult){
                            editPointOfInterestViewModel.editPointOfInterest(
                                currentPointOfInterest?.id!!,
                                name,
                                description,
                                lat!!,
                                long!!,
                                isManual!!,
                                selectedLocations!!,
                                selectedCategory!!
                            ){resultMessage ->
                                when(resultMessage){
                                    Consts.SUCCESS-> {
                                        navController!!.navigateUp()
                                    }
                                    Consts.ERROR_EXISTING_NAME -> {
                                        showSnackBar = true
                                        errorMessage = nameExistsError
                                    }
                                    Consts.ERROR_EXISTING_POINT_OF_INTEREST -> {
                                        showSnackBar = true
                                        errorMessage = locationExistsError
                                    }
                                    Consts.ERROR_NEED_LOGIN ->{
                                        showSnackBar=true
                                        errorMessage=loginNeededError
                                    }
                                    else -> {
                                        showSnackBar = true
                                        errorMessage = resultMessage
                                    }
                                }
                            }
                        }

                    },
                    modifier = modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.edit_location))
                }

            }

        }
    }

}