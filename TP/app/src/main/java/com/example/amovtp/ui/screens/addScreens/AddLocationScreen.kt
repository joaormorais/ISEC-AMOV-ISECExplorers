package com.example.amovtp.ui.screens.addScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import com.example.amovtp.R
import com.example.amovtp.ui.composables.CameraImage
import com.example.amovtp.ui.composables.GalleryImage
import com.example.amovtp.ui.composables.GeoDescription
import com.example.amovtp.ui.composables.NameDescription
import com.example.amovtp.ui.viewmodels.addViewModels.AddLocationViewModel

@Composable
fun AddLocationScreen(
    addLocationViewModel: AddLocationViewModel,
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
                val uniqueImgs = newImgs.filter { !imgsGallery.contains(it) }
                imgsGallery = uniqueImgs
            })
            Spacer(modifier = modifier.height(8.dp))
            CameraImage(imagesPathChanged = { newImgs ->
                val uniqueImgs = newImgs.filter { !imgsCamera.contains(it) }
                imgsCamera = uniqueImgs
            })

            Button(
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
                    }

                },
                modifier = modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.add_loc))
            }

        }

    }

}