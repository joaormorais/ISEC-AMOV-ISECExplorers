package com.example.amovtp.ui.screens.addScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.amovtp.ui.composables.AddInfoComposables.CameraImage
import com.example.amovtp.ui.composables.AddInfoComposables.GalleryImage
import com.example.amovtp.ui.composables.AddInfoComposables.GeoDescription
import com.example.amovtp.ui.composables.AddInfoComposables.NameDescription
import com.example.amovtp.ui.viewmodels.addViewModels.AddLocationViewModel
import com.example.amovtp.utils.Consts

@Composable
fun AddLocationScreen(
    addLocationViewModel: AddLocationViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf<Double?>(null) }
    var long by remember { mutableStateOf<Double?>(null) }
    var isManual by remember { mutableStateOf(true) }
    var imgsGallery by remember { mutableStateOf(listOf<String>())}
    var imgsCamera by remember { mutableStateOf(listOf<String>())}

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackBar by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val unkownError = stringResource(R.string.unknown_error)
    val fillNameError = stringResource(R.string.invalid_name)
    val fillDescriptionError = stringResource(R.string.invalid_description)
    val fillCoordinatesError = stringResource(R.string.invalid_coordinates)
    val fillImagesError = stringResource(R.string.invalid_images)


    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            snackbarHostState.showSnackbar(errorMessage ?: unkownError)
            showSnackBar = false
        }
    }


    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier=modifier.padding(top = 32.dp)
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
                            val tempLocation = addLocationViewModel.getCurrentLocation()
                            lat = tempLocation.value!!.latitude
                            long = tempLocation.value!!.longitude
                        }
                    }
                )
                Spacer(modifier = modifier.height(8.dp))
                GalleryImage(imagesPathChanged = { newImgs ->
                    imgsGallery = newImgs
                })
                Spacer(modifier = modifier.height(8.dp))
                CameraImage(imagesPathChanged = { newImgs ->
                    imgsCamera = newImgs
                })

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Consts.CONFIRMATION_COLOR, contentColor = Color.Black),
                onClick = {

                        val validationResult = isAddLocationValid(
                            name,
                            description,
                            lat,
                            long,
                            isManual,
                            imgsGallery,
                            imgsCamera,
                            fillNameError,
                            fillDescriptionError,
                            fillCoordinatesError,
                            fillImagesError
                        ){ msg ->
                            errorMessage = msg
                        }

                        if(validationResult){
                            val mixedImgs = imgsGallery + imgsCamera

                            //TODO: receber os erros e mostrar na toast (sandra)
                            addLocationViewModel.addLocation(
                                name,
                                description,
                                lat!!,
                                long!!,
                                isManual,
                                mixedImgs
                            )

                            navController!!.navigateUp()
                        }
                        else{
                            // a validação falhou
                            showSnackBar = true
                        }
                    },
                    modifier = modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.add_loc))
                }

            }

        }
    }
}

fun isAddLocationValid(
    name: String,
    description: String,
    lat: Double?,
    long: Double?,
    isManualCoords: Boolean,
    imgsGallery: List<String>,
    imgsCamera: List<String>,
    fillNameError : String,
    fillDescriptionError : String,
    fillCoordinatesError : String,
    fillImagesError: String,
    errorMessage: (String) -> Unit
): Boolean {
    if(name.isBlank()){
        errorMessage(fillNameError)
        return false
    }
    if (description.isBlank()){
        errorMessage(fillDescriptionError)
        return false
    }
    if(isManualCoords && (lat == null || long == null)){
        errorMessage(fillCoordinatesError)
        return false
    }
    if(imgsGallery.isEmpty() && imgsCamera.isEmpty()){
        errorMessage(fillImagesError)
        return false
    }
    return true
}