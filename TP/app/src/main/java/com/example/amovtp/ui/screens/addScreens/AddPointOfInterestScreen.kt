package com.example.amovtp.ui.screens.addScreens

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
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.amovtp.ui.composables.AddInfoComposables.CameraImage
import com.example.amovtp.ui.composables.AddInfoComposables.GalleryImage
import com.example.amovtp.ui.composables.AddInfoComposables.GeoDescription
import com.example.amovtp.ui.composables.AddInfoComposables.NameDescription
import com.example.amovtp.ui.viewmodels.addViewModels.AddPointOfInterestViewModel
import com.example.amovtp.utils.Consts

@Composable
fun AddPointOfInterestScreen(
    addPointOfInterestViewModel: AddPointOfInterestViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf<Double?>(null) }
    var long by remember { mutableStateOf<Double?>(null) }
    var isManual by remember { mutableStateOf(true) }
    var imgsGallery by remember { mutableStateOf(listOf<String>()) }
    var imgsCamera by remember { mutableStateOf(listOf<String>()) }

    var selectedLocations by remember { mutableStateOf(mutableListOf<String>()) }
    var expanded1 by remember { mutableStateOf(false) }
    var locationList: MutableState<List<Location>> = addPointOfInterestViewModel.getLocations()
    var selectedCategory by remember { mutableStateOf("") }
    var expanded2 by remember { mutableStateOf(false) }
    var categoryList: MutableState<List<Category>> = addPointOfInterestViewModel.getCategories()

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val unkownError = stringResource(R.string.unknown_error)
    val fillNameError = stringResource(R.string.invalid_name)
    val fillDescriptionError = stringResource(R.string.invalid_description)
    val fillCoordinatesError = stringResource(R.string.invalid_coordinates)
    val fillImagesError = stringResource(R.string.invalid_images)
    val fillLocationError = stringResource(R.string.invalid_location)
    val fillCategoryError = stringResource(R.string.invalid_category)

    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            snackbarHostState.showSnackbar(errorMessage ?: unkownError)
            showSnackBar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier.padding(top = 32.dp)
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {

                NameDescription(
                    nameChanged = { newName ->
                        name = newName
                    },
                    descriptionChanged = { newDescription ->
                        description = newDescription
                    }
                )
                Spacer(modifier = modifier.height(8.dp))
                GeoDescription(
                    latChanged = { newLat ->
                        lat = newLat
                    },
                    longChanged = { newLong ->
                        long = newLong
                    },
                    manualChanged = { newManual ->
                        isManual = newManual
                        if (!newManual) {
                            val tempLocation = addPointOfInterestViewModel.getCurrentLocation()
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
                                var isLocationPicked by remember { mutableStateOf(selectedLocations.contains(location.name)) }

                                DropdownMenuItem(
                                    text = {
                                        if (isLocationPicked) Text(
                                            location.name,
                                            color = Color.Green
                                        ) else Text(location.name)
                                    },
                                    onClick = {
                                        if (!selectedLocations.removeAll { it == location.name }) {
                                            selectedLocations.add(location.name)
                                            isLocationPicked = true
                                        } else {
                                            selectedLocations.remove(location.name)
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
                                Text(if (selectedCategory.isEmpty()) stringResource(R.string.choose_a_category) else selectedCategory)
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
                Spacer(modifier = modifier.height(8.dp))
                GalleryImage(imagesPathChanged = { newImgs ->
                    val uniqueImgs = newImgs.filter { !imgsGallery.contains(it) }
                    imgsGallery = uniqueImgs
                })
                Spacer(modifier = modifier.height(8.dp))
                CameraImage(imagesPathChanged = { newImgs ->
                    val uniqueImgs = newImgs.filter { !imgsCamera.contains(it) }
                    imgsCamera = uniqueImgs
                })


                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Consts.CONFIRMATION_COLOR,
                        contentColor = Color.Black
                    ),
                    onClick = {

                        val validationResult = isAddPointOfInterestValid(
                            name,
                            description,
                            lat,
                            long,
                            isManual,
                            selectedLocations,
                            selectedCategory,
                            imgsGallery,
                            imgsCamera,
                            fillNameError,
                            fillDescriptionError,
                            fillCoordinatesError,
                            fillImagesError,
                            fillLocationError,
                            fillCategoryError
                        ) { msg ->
                            errorMessage = msg
                        }

                        if (validationResult) {
                            val mixedImgs = imgsGallery + imgsCamera

                            //TODO: receber os erros e mostrar na toast (sandra)
                            addPointOfInterestViewModel.addPointOfInterest(
                                name,
                                description,
                                lat!!,
                                long!!,
                                isManual,
                                selectedLocations,
                                selectedCategory,
                                mixedImgs
                            ){
                                resultMessage ->
                                showSnackBar = true
                                errorMessage = resultMessage
                            }

                            navController!!.navigateUp()
                        } else {
                            showSnackBar = true
                        }
                    },
                    modifier = modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.add_point))
                }
            }
        }
    }
}

fun isAddPointOfInterestValid(
    name: String,
    description: String,
    lat: Double?,
    long: Double?,
    isManualCoords: Boolean,
    selectedLocations: List<String>,
    selectedCategory: String,
    imgsGallery: List<String>,
    imgsCamera: List<String>,
    fillNameError: String,
    fillDescriptionError: String,
    fillCoordinatesError: String,
    fillImagesError: String,
    fillLocationError: String,
    fillCategoryError: String,
    errorMessage: (String) -> Unit
): Boolean {
    if (name.isBlank()) {
        errorMessage(fillNameError)
        return false
    }
    if (description.isBlank()) {
        errorMessage(fillDescriptionError)
        return false
    }
    if (lat == null || long == null) {
        errorMessage(fillCoordinatesError)
        return false
    }
    if (selectedLocations.isEmpty()) {
        errorMessage(fillLocationError)
        return false
    }
    if (selectedCategory.isBlank()) {
        errorMessage(fillCategoryError)
        return false
    }
    if (imgsGallery.isEmpty() && imgsCamera.isEmpty()) {
        errorMessage(fillImagesError)
        return false
    }
    return true
}