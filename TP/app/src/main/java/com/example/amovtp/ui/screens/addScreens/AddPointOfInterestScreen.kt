package com.example.amovtp.ui.screens.addScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


@Composable
fun AddPointOfInterestScreen(
    addPointOfInterestViewModel: AddPointOfInterestViewModel,
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

    var selectedLocation by remember { mutableStateOf("") }
    var expanded1 by remember { mutableStateOf(false) }
    var locationList: List<Location> = addPointOfInterestViewModel.getLocations()
    var selectedCategory by remember { mutableStateOf("") }
    var expanded2 by remember { mutableStateOf(false) }
    var categoryList: List<Category> = addPointOfInterestViewModel.getCategories()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item{

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
            //TODO: fazer um dropdown menu com todas as localizações e quando o utilizador escolhe uma, manda para a função de adicionar ponto de interesse

            // DropdownMenu para selecionar a localização
            Box(Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = { expanded1 = true },
                ) {

                    Text(if (selectedLocation.isEmpty()) "Escolha uma localização" else selectedLocation)
                }

                DropdownMenu(
                    expanded = expanded1,
                    onDismissRequest = { expanded1 = false }
                ) {
                    locationList.forEach { location ->
                        DropdownMenuItem(
                            text = {Text(location.name)},
                            onClick = {
                            selectedLocation = location.name
                            expanded1 = false
                        })
                    }
                }
            }

            // DropdownMenu para selecionar a categoria
            Box(Modifier.fillMaxWidth()) {
                TextButton(onClick = { expanded2 = true }) {
                    Text(if (selectedCategory.isEmpty()) "Escolha uma categoria" else selectedCategory)
                }

                DropdownMenu(
                    expanded = expanded2,
                    onDismissRequest = { expanded2 = false }
                ) {
                    categoryList.forEach { category ->
                        DropdownMenuItem(
                            text = {Text(category.name)},
                            onClick = {
                            selectedCategory = category.name
                            expanded2 = false
                        })
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
                    } else if(selectedLocation.isBlank()){
                        //TODO: pop up de erro a dizer que é preciso selecionar localizacao
                    }else if(selectedCategory.isBlank()){
                        //TODO: pop up de erro a dizer que é preciso selecionar Categoria
                    } else {

                        val mixedImgs = imgsGallery + imgsCamera

                        addPointOfInterestViewModel.addPointOfInterest(
                            name,
                            description,
                            lat!!,
                            long!!,
                            isManual,
                            selectedLocation,
                            selectedCategory,
                            mixedImgs
                        )

                        navController!!.navigateUp()
                    }
                },
                modifier = modifier.padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.add_point))
            }

        }

    }

}