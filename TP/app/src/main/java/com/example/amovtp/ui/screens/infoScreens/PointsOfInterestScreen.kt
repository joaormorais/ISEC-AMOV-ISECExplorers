package com.example.amovtp.ui.screens.infoScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.amovtp.R
import com.example.amovtp.data.Category
import com.example.amovtp.ui.viewmodels.infoViewModels.PointsOfInterestViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointsOfInterestScreen(
    pointsOfInterestViewModel: PointsOfInterestViewModel,
    itemName: String? = null,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isExpandedLocations by remember { mutableStateOf(false) }
    var isExpandedCategories by remember { mutableStateOf(false) }
    var selectedIndexLocations by remember { mutableStateOf(0) }
    var selectedIndexCategories by remember { mutableStateOf(0) }
    val defaultString = stringResource(R.string.defaultvalue)
    val allLocationsString = stringResource(R.string.all_locations)
    val allCategoriesString = stringResource(R.string.all_categories)
    val currentLat = 39.694460831786216 //TODO: trocar para a loc atual do dispositivo
    val currentLong = -8.130543343335995 //TODO: trocar para a loc atual do dispositivo
    val locations = pointsOfInterestViewModel.getLocations()
    val categories = pointsOfInterestViewModel.getCategories()
    var pointsOfInterest by remember {
        mutableStateOf(pointsOfInterestViewModel.getPointsOfInterest())
    }

    LaunchedEffect(key1 = itemName) {
        if (itemName != null)
            if (itemName != defaultString)
                pointsOfInterest = pointsOfInterestViewModel.getPointsFromLocation(itemName)
    }

    val selectedLocation by remember {
        mutableStateOf(
            if (itemName != defaultString) {
                locations.find { it.name == itemName }
            } else {
                null
            }
        )
    }
    val selectedCategory by remember {
        mutableStateOf<Category?>(null)
    }
    var selectedLocationName by remember {
        mutableStateOf(
            if (selectedLocation != null)
                selectedLocation!!.name
            else
                allLocationsString
        )
    }
    var selectedCategoryName by remember {
        mutableStateOf(
            if (selectedCategory != null)
                selectedCategory!!.name
            else
                allCategoriesString
        )
    }
    var geoPoint by remember {
        mutableStateOf(
            if (itemName != defaultString)
                GeoPoint(selectedLocation!!.lat, selectedLocation!!.long)
            else
                GeoPoint(currentLat, currentLong)

        )
    }


    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
        ){
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(end = 8.dp)
            ) {
                Text(
                    selectedLocationName!!,
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable(onClick = { isExpandedLocations = true })
                )
                DropdownMenu(
                    expanded = isExpandedLocations,
                    onDismissRequest = { isExpandedLocations = false },
                    modifier = Modifier
                        .wrapContentWidth()
                        .heightIn(max = 200.dp)
                        .wrapContentHeight(Alignment.Top)
                ) {
                    DropdownMenuItem(
                        text = { Text(allLocationsString) },
                        onClick = {
                            selectedLocationName = allLocationsString
                            selectedIndexLocations = 0
                            isExpandedLocations = false
                            pointsOfInterest =
                                pointsOfInterestViewModel.getPointsOfInterest()
                        }
                    )
                    pointsOfInterestViewModel.getLocations().forEachIndexed { index, location ->
                        DropdownMenuItem(
                            text = { Text(text = location.name) },
                            onClick = {
                                selectedLocationName = location.name
                                selectedIndexLocations = index + 1
                                isExpandedLocations = false
                                pointsOfInterest =
                                    pointsOfInterestViewModel.getPointsFromLocation(location.name)
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
                    .padding(start = 8.dp)
            ) {
                Text(
                    selectedCategoryName!!,
                    modifier = Modifier
                        .wrapContentWidth()
                        .clickable(onClick = { isExpandedCategories = true })
                )

                DropdownMenu(
                    expanded = isExpandedCategories,
                    onDismissRequest = { isExpandedCategories = false },
                    modifier = Modifier
                        .wrapContentWidth()
                        .heightIn(max = 200.dp)
                        .wrapContentHeight(Alignment.Top)
                ) {
                    DropdownMenuItem(
                        text = { Text(allCategoriesString) },
                        onClick = {
                            selectedCategoryName = allCategoriesString
                            selectedIndexCategories = 0
                            isExpandedCategories = false
                            pointsOfInterest = pointsOfInterestViewModel.getPointsOfInterest()
                        }
                    )
                    pointsOfInterestViewModel.getCategories().forEachIndexed { index, categories ->
                        DropdownMenuItem(
                            text = { Text(text = categories.name) },
                            onClick = {
                                selectedCategoryName = categories.name
                                selectedIndexCategories = index + 1
                                isExpandedCategories = false
                                pointsOfInterest =
                                    pointsOfInterestViewModel.getPointsFromCategory(categories.name)
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxSize(0.5f)
                .clipToBounds()
                .background(
                    Color(255, 240, 128)
                )
        ) {
            AndroidView(factory = { context ->

                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(18.0)
                    controller.setCenter(geoPoint)
                    for (i in pointsOfInterestViewModel.getPointsOfInterest()) {
                        overlays.add(
                            Marker(this).apply {
                                position = GeoPoint(i.lat, i.long)
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = i.name
                            }
                        )
                    }
                }

            },
                update = {
                    it.controller.setCenter(geoPoint)
                })
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(pointsOfInterest, key = { it.id }) {

                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    onClick = {
                        geoPoint = GeoPoint(it.lat, it.long)
                    }
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        //TODO: caso esta informação pertença a um utilizador, deve aparecer o botão de editar
                        Text(
                            text = it.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.description, it.description),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}