package com.example.amovtp.ui.screens.addScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    var name = ""
    var description = ""
    var lat: Double? = null
    var long: Double? = null
    var isManual: Boolean = true
    var imgsGallery: List<String> = emptyList()
    var imgsCamera: List<String> = emptyList()


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
                colors = ButtonDefaults.buttonColors(containerColor = Consts.CONFIRMATION_COLOR, contentColor = Color.DarkGray),
                onClick = {

                    if (name.isBlank()) {
                        //TODO: pop up de erro a dizer name em falta
                    } else if (description.isBlank()) {
                        //TODO: pop up de erro a dizer description em falta
                    } else if (lat == null) {
                        //TODO: pop up de erro a dizer lat em falta
                    } else if (long == null) {
                        //TODO: pop up de erro a dizer long em falta
                    } else if (imgsGallery.isEmpty() && imgsCamera.isEmpty()) {
                        //TODO: pop up de erro a dizer que é preciso pelo menos uma imagem
                    } else {

                        val mixedImgs = imgsGallery + imgsCamera

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

                },
                modifier = modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.add_loc))
            }

        }

    }

}